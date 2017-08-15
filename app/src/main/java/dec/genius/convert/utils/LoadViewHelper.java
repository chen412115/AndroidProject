package dec.genius.convert.utils;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import dec.genius.convert.R;
import com.shizhefei.view.vary.IVaryViewHelper;
import com.shizhefei.view.vary.VaryViewHelper;


/**
 * 自定义要切换的布局，通过IVaryViewHelper实现真正的切换<br>
 * 使用者可以根据自己的需求，使用自己定义的布局样式
 * 
 * @author LuckyJayce
 *
 */
public class LoadViewHelper {

	private IVaryViewHelper helper;

	public LoadViewHelper(View view) {
		this(new VaryViewHelper(view));
	}

	public LoadViewHelper(IVaryViewHelper helper) {
		super();
		this.helper = helper;
	}
	public void showError(OnClickListener onClickListener) {
		View layout = helper.inflate(R.layout.load_error);
        layout.setOnClickListener(onClickListener);
		helper.showLayout(layout);
	}
	public void showError(String errorText, OnClickListener onClickListener) {
		View layout = helper.inflate(R.layout.load_error);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setText(errorText);
        layout.setOnClickListener(onClickListener);
		helper.showLayout(layout);
	}
	public void showEmpty(OnClickListener onClickListener) {
		View layout = helper.inflate(R.layout.load_empty);
        layout.setOnClickListener(onClickListener);
		helper.showLayout(layout);
	}
	public void showEmpty(String errorText, OnClickListener onClickListener) {
		View layout = helper.inflate(R.layout.load_empty);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setText(errorText);
        layout.setOnClickListener(onClickListener);
		helper.showLayout(layout);
	}
	public void showLoading() {
		View layout = helper.inflate(R.layout.load_ing);
		helper.showLayout(layout);
	}
	public void showLoading(String loadText) {
		View layout = helper.inflate(R.layout.load_ing);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setText(loadText);
		helper.showLayout(layout);
	}
	public void showLoading(int resId) {
		View layout = helper.inflate(R.layout.load_ing);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setText(resId);
		helper.showLayout(layout);
	}

	public void showNetworkError(OnClickListener onClickListener){
		View layout = helper.inflate(R.layout.load_empty);

		layout.setOnClickListener(onClickListener);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setText("网络异常");
		helper.showLayout(layout);
	}

	public void restore() {
		helper.restoreView();
	}
}
