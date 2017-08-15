package dec.genius.convert.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;

import com.apkfuns.xprogressdialog.XProgressDialog;
import dec.genius.convert.utils.AppPreferences;
import dec.genius.convert.utils.MyHttpRequestCallBack;
import dec.genius.convert.utils.ToastUtil;

public abstract class BaseFragment extends BaseLazyFragment implements BaseView,MyHttpRequestCallBack.HttpCallbackResult {

    @Override
    public void showError(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showException(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showNetError() {
        toggleNetworkError(true, null);
    }

    @Override
    public void showLoading(String msg) {
        toggleShowLoading(true, null);
    }
    XProgressDialog mProgressDialog;

    /*
     * 显示进度框
     */
    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new XProgressDialog(mContext);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMessage(msg);
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void showProgressDialog(String msg, boolean cancleble) {
        if (mProgressDialog == null) {
            mProgressDialog = new XProgressDialog(mContext);
            mProgressDialog.setCancelable(cancleble);
            mProgressDialog.setMessage(msg);
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    /*
     * 隐藏进度框
     */
    public void cancelProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.cancel();
    }

    @Override
    public void onDestroy() {
        cancelProgressDialog();
        super.onDestroy();
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, null);
    }

    //检查登录
    public boolean checkLogin() {
        if (TextUtils.isEmpty(AppPreferences.getString("uid"))) {
            return false;
        }
        return true;
    }
    protected   String getUserId(){
        return AppPreferences.getString("uid");
    }
    public void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onRequestFinished(boolean isError, String str, int requestCode) {
        if(isError){
            ToastUtil.showToast(mContext,str);
            return;
        }
    }
}
