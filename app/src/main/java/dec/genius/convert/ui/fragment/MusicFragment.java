package dec.genius.convert.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import dec.genius.convert.R;
import dec.genius.convert.base.BaseFragment;
import dec.genius.convert.bean.Song;
import dec.genius.convert.ui.adapter.AudioRecyclerViewAdapter;
import dec.genius.convert.utils.AudioUtils;
import dec.genius.convert.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_music)
public class MusicFragment extends BaseFragment {
    List<Song> songs; //存放本地视频的
    @ViewInject(R.id.audio_recyclerView)
    RecyclerView recyclerView;
    AudioRecyclerViewAdapter adapter;
    OnClickItemListener listener;


    public MusicFragment() {
        // Required empty public constructor
    }
    public void setOnClickItemListener(OnClickItemListener listener){
        this.listener = listener;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        songs = AudioUtils.getAllSongs(getActivity());
        L.e( "所有的歌曲："+songs.toString() );
        initView();
        initData();
        initListener();
    }
    private void initListener() {
        adapter.setOnItemClickListener(new AudioRecyclerViewAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                L.e("音乐信息是: "+ songs.get(position).toString() );
                listener.onClick(songs.get(position),position);
            }
        });
    }

    private void initData() {

    }

    private void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2)); // 设置样式
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //动画有四种，分别是移动、改变、添加、删除
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL)); //添加分割线
        adapter = new AudioRecyclerViewAdapter(songs);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnClickItemListener) activity;
        } catch (ClassCastException e) {

        }
    }
    public interface OnClickItemListener{
        void onClick(Song songs,int position);
    }
}
