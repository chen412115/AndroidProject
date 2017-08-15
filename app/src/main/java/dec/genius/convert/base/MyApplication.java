package dec.genius.convert.base;

import android.app.ActivityManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.lib_cxq_common.MyConstant;

import org.xutils.BuildConfig;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/18 0018.
 * 1.检测网络是否可用
 * 2.获取到图片的完整地址
 * 3.获取程序名称
 * 4.获取版本名和版本号
 * 5.判断是否前台运行
 * 6.判断手机是否处理睡眠
 */

public class MyApplication extends Application {
    public static final String TAG = "MyApplication";
    static MyApplication instance;
    public static boolean mIsNetworkAvailable = false;
    public static boolean isDevelop = true;
    private static DbManager dm;
    public static final String HOST = "";


    //创建xutils的数据库
    static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("kanyikan.db")
//            .setDbDir(new File("/sdcard/huiyou"))
//            .setDbDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/hy"))
            .setDbVersion(1)
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    LogUtil.e("updateDataBase");
                }
            });
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initXUtils();

    }
    public static MyApplication getInstance(){
        return instance;
    }
    private void initXUtils(){
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        dm = x.getDb(daoConfig);
    }

    /**
     * 获取到图片的完整地址
     * @param stringList
     * @return
     */
    public static List<String> getFullImageURLs(List<String> stringList){
        List<String> urls = new ArrayList<>();
        if(MyConstant.listNotNull(stringList)){
            for(String url:stringList){
                urls.add(getFullImageURL(url));
            }
        }
        return urls;
    }
    public static String getFullImageURL(String imgstr){
        return MyApplication.HOST+imgstr;
    }
    /*
    * xutils 进行网络请求
    * */
    public static <T> Callback.Cancelable sendRequestGET(RequestParams params, Callback.CommonCallback<T> callback){
        return  sendRequest(HttpMethod.GET,params,callback);
    }

    public static <T> Callback.Cancelable sendRequestPOST(RequestParams params, Callback.CommonCallback<T> callback){
        return  sendRequest(HttpMethod.POST,params,callback);
    }
    public static <T> Callback.Cancelable sendRequest(HttpMethod httpMethod,RequestParams params, Callback.CommonCallback<T> callback){
        return  x.http().request(httpMethod,params,callback);
    }
    public static <T> Callback.Cancelable sendRequest(RequestParams params, Callback.CommonCallback<T> callback){
        return  sendRequest(HttpMethod.POST,params,callback);
    }


    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
    /*
    获取版本号
    * */
    public static int getVersionCode(Context context) {
        try {
            PackageManager e = context.getPackageManager();
            PackageInfo packageInfo = e.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
            return 0;
        }
    }
    /*获取版本名
    * */
    public static String getVersionName(Context context) {
        try {
            PackageManager e = context.getPackageManager();
            PackageInfo packageInfo = e.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
            return null;
        }
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
        } catch (PackageManager.NameNotFoundException e) {
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
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
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
}
