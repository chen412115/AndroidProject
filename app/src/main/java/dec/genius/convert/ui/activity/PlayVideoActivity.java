package dec.genius.convert.ui.activity;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import org.xutils.view.annotation.ContentView;

import dec.genius.convert.R;
import dec.genius.convert.base.BaseActivity;
import dec.genius.convert.ijkplayer.common.PlayerManager;
import dec.genius.convert.ijkplayer.playerview.widget.PlayerView;
import dec.genius.convert.netstatus.NetUtils;
@ContentView(R.layout.activity_play_video)
public class PlayVideoActivity extends BaseActivity implements PlayerManager.PlayerStateListener{
    private String url1 ;//= "/storage/emulated/0/tencent/MicroMsg/affac3c84bedbd9063354676b8f62406/video/1856001608170a60ea2627.mp4";
    private PlayerManager player;
    //PlayerView player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

    @Override
    protected void initViewsAndEvents() {
        url1 = getIntent().getStringExtra("selectVideo");
        initPlayer();
    }
    private void initPlayer() {
        player = new PlayerManager(this);
        player.setFullScreenOnly(true);
        player.setScaleType(PlayerManager.SCALETYPE_FILLPARENT);
        player.playInFullScreen(true);
        player.setPlayerStateListener(this);
        player.play(url1);
    }
    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (player.gestureDetector.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }
    //
    @Override
    public void onComplete() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onPlay() {

    }
}
