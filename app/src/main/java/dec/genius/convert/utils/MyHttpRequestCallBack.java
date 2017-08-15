package dec.genius.convert.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.shizhefei.mvc.RequestHandle;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import dec.genius.convert.bean.BaseEntity;
/**
 * 网络请求回调
 * @ClassName: MyHttpRequestCallBack
 * @Description: TODO
 * @author 王富生
 * @date 2015年3月24日 上午10:09:58
 *
 */
public class MyHttpRequestCallBack implements Callback.CommonCallback<String>,RequestHandle {
	public static final String TAG = MyHttpRequestCallBack.class.getSimpleName();
	private HttpCallbackResult callbackResult;
	private int requestCode = 0;
	private String result = "";
	private boolean hasError = false;
	public interface HttpCallbackResult {
		 void onRequestFinished(boolean isError, String str, int requestCode);
	};

	public MyHttpRequestCallBack(Context context, int requestCode) {

		this.requestCode = requestCode;
		try {
			this.callbackResult = (HttpCallbackResult) context;
		} catch (Exception e) {
			throw new ClassCastException(context.toString()+ " Must implent SuccessResult");
		}

	}
	public MyHttpRequestCallBack(Fragment context, int requestCode) {

		this.requestCode = requestCode;
		try {
			this.callbackResult = (HttpCallbackResult) context;
		} catch (Exception e) {
			throw new ClassCastException(context.toString()+ " Must implent SuccessResult");
		}
	}

	@Override
	public void onFinished() {
		callbackResult.onRequestFinished(hasError, result, requestCode);
	}

	@Override
	public void onSuccess(String result) {
		Log.d(TAG, requestCode + ":" + result);


		BaseEntity object = JsonUtil.getObjectFromString(result,BaseEntity.class);

		if(object==null) {
			int status = object.getStatus();
			String msg = object.getMessage();
			String content = object.getResult();
			hasError = (status!=1);
			this.result = hasError ? msg :content;

		}else{
			hasError = true;
			this.result = "数据解析失败";
		}

	}



	@Override
	public void onError(Throwable ex, boolean isOnCallback) {
		hasError = true;
		if (ex instanceof HttpException) {
			// 网络错误
			HttpException httpEx = (HttpException) ex;
			int responseCode = httpEx.getCode();
			result = "网络异常";
			String responseMsg = httpEx.getMessage();
			String errorResult = httpEx.getResult();
		} else { // 其他错误
			result = "服务器异常";
		}
	}

	@Override
	public void onCancelled(CancelledException cex) {

	}

	@Override
	public void cancle() {
		CancelledException cancelledException = new CancelledException("取消请求");
		onCancelled(cancelledException);
	}

	@Override
	public boolean isRunning() {
		return false;
	}
}
