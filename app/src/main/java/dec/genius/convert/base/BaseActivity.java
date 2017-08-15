package dec.genius.convert.base;

import android.text.TextUtils;

import dec.genius.convert.utils.AppPreferences;


public abstract class BaseActivity extends BaseFragmentActivity implements BaseView {

    protected MyApplication getBaseApplication() {
        return (MyApplication) getApplication();
    }

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
        toggleShowLoading(true, msg);
    }

    @Override
    public void hideLoading() {
        toggleShowLoading(false, null);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected boolean isFullScreen() {
        return false;
    }

    //检查登录
    public boolean checkLogin() {
        if (TextUtils.isEmpty(AppPreferences.getString("userId"))) {
            return false;
        }
        return true;
    }
}
