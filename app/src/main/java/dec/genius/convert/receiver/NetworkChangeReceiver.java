package dec.genius.convert.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import dec.genius.convert.base.MyApplication;
import dec.genius.convert.utils.ToastUtil;

/**
 * 
 * @description NetworkChangeReceiver
 * @author WangFusheng
 * @version 1.0
 * @time 2014-7-19
 */
public class NetworkChangeReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		State wifiState = null;
		State mobileState = null;
		boolean IsNetworkAvailable = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (wifiState != null && mobileState != null
				&& State.CONNECTED != wifiState
				&& State.CONNECTED == mobileState) {
			// 手机网络连接成功
			ToastUtil.showToast(context, "连接到手机网络，请注意流量");
			IsNetworkAvailable = true;
		} else if (wifiState != null && mobileState != null
				&& State.CONNECTED != wifiState
				&& State.CONNECTED != mobileState) {
			// 手机没有任何的网络
			ToastUtil.showToast(context, "网络连接失败，请检测网络状态");
			IsNetworkAvailable = false;
			
		} else if (wifiState != null && State.CONNECTED == wifiState) {
			// 无线网络连接成功
			ToastUtil.showToast(context, "已连接WiFi网络，请放心使用");
			IsNetworkAvailable = true;
		}
		MyApplication.mIsNetworkAvailable = IsNetworkAvailable;

	}

}
