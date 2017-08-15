package dec.genius.convert.ui.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lib_cxq_common.widget.CircleImageView;

import org.xutils.view.annotation.ContentView;

import java.io.IOException;

import dec.genius.convert.R;
import dec.genius.convert.base.BaseFragment;
import dec.genius.convert.bean.Song;
import dec.genius.convert.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_music_play)
public class MusicPlayFragment extends BaseFragment implements View.OnClickListener{
    SeekBar seekbar ;
    CircleImageView image;
    TextView song,singer;
    ImageView play ,next;
    View view;
    MediaPlayer mediaPlayer;
    Song currentSong;
    long duration ;
    boolean isPlaying = false;
    final int UPDATE_PROGRESS=100001;
    int currentSongIndex;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_PROGRESS:
                    try {
                        seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //handler.sendEmptyMessageDelayed(UPDATE);
                    sendEmptyMessageDelayed(UPDATE_PROGRESS,500);
                    break;
            }
        }
    };

    public MusicPlayFragment() {
        // Required empty public constructor
    }


    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        initView ();
        initListener();
    }
    private void initListener() {
        image.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
    }



    private void initView() {
        image = (CircleImageView) view.findViewById(R.id.touxiang);
        seekbar = (SeekBar) view.findViewById(R.id.seekbar);
        song = (TextView) view.findViewById(R.id.song);
        singer = (TextView) view.findViewById(R.id.singer);
        play = (ImageView) view.findViewById(R.id.play);
        next = (ImageView) view.findViewById(R.id.next);
        mediaPlayer = new MediaPlayer();
    }

    public void onGetData(Song selectedSong,int position){
        //收到消息 ，说明已经开始播放音乐
        currentSong = selectedSong;
        currentSongIndex = position;
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.music_pause);
        ((ImageView)view.findViewById(R.id.play)).setImageBitmap(bm);
        L.e( "onGetData: 你点击的歌曲是："+selectedSong.toString());
        duration = selectedSong.getDuration();
        song.setText(selectedSong.getTitle());
        singer.setText(selectedSong.getSinger());
        seekbar.setMax((int) duration);
        try {
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                mediaPlayer.reset();
            }
            L.e("getFileUrl: "+selectedSong.getFileUrl() );
            mediaPlayer.setDataSource(selectedSong.getFileUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.what = UPDATE_PROGRESS;
        handler.sendMessage(msg);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                L.e( "progress: "+progress );
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    mediaPlayer.start();
                    isPlaying = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.touxiang:

                break;
            case R.id.play:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.music_play);
                    ((ImageView)view.findViewById(R.id.play)).setImageBitmap(bm);
                }else {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.music_pause);
                    ((ImageView)view.findViewById(R.id.play)).setImageBitmap(bm);
                    mediaPlayer.start();
                }
                break;
            case R.id.next:


                break;
            case R.id.pre:


                break;

        }
    }

    int i =1000;
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
