package dec.genius.convert.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.lib_cxq_common.util.IntentUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * @ClassName: AppUtils
 * @Description: app相关的辅助类
 * @author WangFusheng
 * @date 2015年9月1日 上午10:25:57
 */
public class AppUtils {
public static final String TAG = AppUtils.class.getSimpleName();
	private AppUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * need < uses-permission android:name =“android.permission.GET_TASKS” />
	 * 判断是否前台运行
	 */
	public static boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<RunningTaskInfo> taskList = am.getRunningTasks(1);
		if (taskList != null && !taskList.isEmpty()) {
			ComponentName componentName = taskList.get(0).topActivity;
			if (componentName != null&& componentName.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: isSleeping 
	 * @Description:判断手机是否处理睡眠
	 * @author WangFusheng 
	 * @param @param context
	 * @param @return    
	 * @return boolean    
	 * @throws
	 */
	public static boolean isSleeping(Context context) {
		KeyguardManager kgMgr = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
		Log.d(TAG,isSleeping ? "手机睡眠中.." : "手机未睡眠...");
		return isSleeping;
	}

	/**
	 * 
	 * @Title: isPhone 
	 * @Description: 判断是否为手机
	 * @author WangFusheng 
	 * @param @param context
	 * @param @return    
	 * @return boolean    
	 * @throws
	 */
	public static boolean isPhone(Context context) {
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int type = telephony.getPhoneType();
		if (type == TelephonyManager.PHONE_TYPE_NONE) {
			Log.i(TAG,"Current device is Tablet!");
			return false;
		} else {
			Log.i(TAG,"Current device is phone!");
			return true;
		}
	}

	/**
	 * 调用系统安装应用
	 */
	public static boolean install(Context context, File file) {
		if (file == null || !file.exists() || !file.isFile()) {
			return false;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		return true;
	}

	/**
	 * 调用系统卸载应用
	 */
	public static void uninstallApk(Context context, String packageName) {
		Intent intent = new Intent(Intent.ACTION_DELETE);
		Uri packageURI = Uri.parse("package:" + packageName);
		intent.setData(packageURI);
		context.startActivity(intent);
	}

	/**
	 * 打开设置网络界面
	 * */
	public static void getNetworkSetting(final Context context) {
		// 提示对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("网络设置提示").setMessage("请检查你的网络连接")
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						IntentUtil.showSystemSetting(context);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	/**
	 * 回到home，后台运行
	 *
	 * @param context
	 */
	public static void goHome(Context context) {
		Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
		mHomeIntent.addCategory(Intent.CATEGORY_HOME);
		mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		context.startActivity(mHomeIntent);
	}

	/**
	 * 启动应用
	 */
	public static boolean startAppByPackageName(Context context,
			String packageName) {
		return startAppByPackageName(context, packageName, null);
	}

	/**
	 * 启动应用
	 */
	public static boolean startAppByPackageName(Context context,
			String packageName, Map<String, String> param) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
				resolveIntent.setPackage(pi.packageName);
			}

			List<ResolveInfo> apps = context.getPackageManager()
					.queryIntentActivities(resolveIntent, 0);

			ResolveInfo ri = apps.iterator().next();
			if (ri != null) {
				String packageName1 = ri.activityInfo.packageName;
				String className = ri.activityInfo.name;

				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);

				ComponentName cn = new ComponentName(packageName1, className);

				intent.setComponent(cn);
				if (param != null) {
					for (Map.Entry<String, String> en : param.entrySet()) {
						intent.putExtra(en.getKey(), en.getValue());
					}
				}
				context.startActivity(intent);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context.getApplicationContext(), "启动失败",
					Toast.LENGTH_LONG).show();
		}
		return false;
	}

	/**
	 * 获取设备id（IMEI）
	 * @param context
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	public static String getDeviceIMEI(Context context) {
		String deviceId;
		if (isPhone(context)) {
			TelephonyManager telephony = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = telephony.getDeviceId();
		} else {
			deviceId = Settings.Secure.getString(context.getContentResolver(),
					Settings.Secure.ANDROID_ID);

		}
		Log.d(TAG,"当前设备IMEI码: " + deviceId);
		return deviceId;
	}

	/**
	 * 获取设备mac地址
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {
		String macAddress;
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		macAddress = info.getMacAddress();
		Log.d(TAG,"当前mac地址: " + (null == macAddress ? "null" : macAddress));
		if (null == macAddress) {
			return "";
		}
		macAddress = macAddress.replace(":", "");
		return macAddress;
	}

	// 获得独一无二的Psuedo ID
	public static String getUniquePsuedoID() {
		String serial = null;

		@SuppressWarnings("deprecation")
		String m_szDevIDShort = "35" + Build.BOARD.length() % 10
				+ Build.BRAND.length() % 10 +

				Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

				Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

				Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

				Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

				Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

				Build.USER.length() % 10; // 13 位

		try {
			serial = Build.class.getField("SERIAL").get(null)
					.toString();
			// API>=9 使用serial号
			return new UUID(m_szDevIDShort.hashCode(), serial.hashCode())
					.toString();
		} catch (Exception exception) {
			// serial需要一个初始化
			serial = "serial"; // 随便一个初始化
		}
		// 使用硬件信息拼凑出来的15位号码
		return new UUID(m_szDevIDShort.hashCode(), serial.hashCode())
				.toString();
	}
	
	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}
	/**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     * 
     * @param context
     * @return if application is in background return true, otherwise return false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
