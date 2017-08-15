package dec.genius.convert.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lib_cxq_common.AppManager;
import com.lib_cxq_common.util.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


import dec.genius.convert.R;
import dec.genius.convert.base.BaseActivity;
import dec.genius.convert.netstatus.NetUtils;
import dec.genius.convert.ui.fragment.MusicFragment;
import dec.genius.convert.ui.fragment.PictureFragment;
import dec.genius.convert.ui.fragment.VideoFragment;

@ContentView(R.layout.activity_first)
public class FirstActivity extends BaseActivity {
    @ViewInject(R.id.rb_video)RadioButton rb_video;
    @ViewInject(R.id.rg)RadioGroup rg;
    FragmentManager manager;
    FragmentTransaction transaction;
    Fragment videoFragment,pictureFragment,musicFragment,currentFragment;
    List<Fragment> fragments;


    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @Override
    protected int getContentViewLayoutID() {
        return 0;
    }
    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Event(type = View.OnClickListener.class,value = {R.id.tv_setting,R.id.tv_share})
    private void click(View view){
        switch (view.getId()){
            case R.id.tv_setting: //进入设置

                break;
            case R.id.tv_share://进行分享

                break;
        }
    }
    @Override
    protected void initViewsAndEvents() {
        currentFragment = null;
        fragments = new ArrayList<>();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        videoFragment = new VideoFragment();
        transaction.add(R.id.container,videoFragment);
        transaction.commit();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = manager.beginTransaction();
                switch (checkedId){
                    case R.id.rb_video:
                        if (videoFragment == null){
                            videoFragment = new VideoFragment();
                            transaction.add(R.id.container,videoFragment);
                        }
                        currentFragment = videoFragment;
                        transaction.show(currentFragment);
                        if (musicFragment!=null){
                            transaction.hide(musicFragment);
                        }if (pictureFragment!=null){
                        transaction.hide(pictureFragment);
                    }
                        break;
                    case R.id.rb_music:
                        if (musicFragment == null){
                            musicFragment = new MusicFragment();
                            transaction.add(R.id.container,musicFragment);
                        }
                        currentFragment = musicFragment;
                        transaction.show(currentFragment);
                        if (videoFragment != null){
                            transaction.hide(videoFragment);
                        }if (pictureFragment != null){
                        transaction.hide(pictureFragment);
                    }
                        break;
                    case R.id.rb_picture:
                        if (pictureFragment == null){
                            pictureFragment = new PictureFragment();
                            transaction.add(R.id.container,pictureFragment);
                        }
                        currentFragment = pictureFragment;
                        transaction.show(currentFragment);
                        if (videoFragment!=null){
                            transaction.hide(videoFragment);
                        }if (musicFragment!=null){
                        transaction.hide(musicFragment);
                    }
                        break;
                }
                transaction.commit();
            }
        });
    }
    long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                exitTime = System.currentTimeMillis();
                ToastUtil.showToast(this, R.string.two_click_exit);
            } else {
                AppManager.getAppManager().AppExit(this);
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        Toast.makeText(mContext, "网络连接", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNetworkDisConnected() {
        Toast.makeText(mContext, "网络断开", Toast.LENGTH_SHORT).show();
    }
}
