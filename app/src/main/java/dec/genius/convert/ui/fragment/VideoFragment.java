package dec.genius.convert.ui.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import dec.genius.convert.R;
import dec.genius.convert.base.BaseFragment;
import dec.genius.convert.ui.adapter.VideoRecyclerViewAdapter;
import dec.genius.convert.utils.AppPreferences;
import dec.genius.convert.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_video)
public class VideoFragment extends BaseFragment {
    public static final int NEW_VIDEOS = 100001,NO_VIDEOS=10002;
    List<File> videos; //存放本地视频的
    @ViewInject(R.id.video_recyclerView)
    RecyclerView recyclerView;
    VideoRecyclerViewAdapter adapter;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            Bundle b = msg.getData();
            File file = (File) b.getSerializable("video");
            switch (what){
                case NEW_VIDEOS: //扫描到了新的视频
                    //videos.add(file);
                    adapter.notifyDataSetChanged();

                    break;
                case NO_VIDEOS: //扫描完成

                    Toast.makeText(getActivity(), "扫描完成....  大小是："+videos.size(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        initView();
        initData();
        new ScannerAnsyTask().execute();
        recyclerView.setAdapter(adapter);
        initListener();
    }

    private void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2)); // 设置样式
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //动画有四种，分别是移动、改变、添加、删除
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL)); //添加分割线
    }

    private void initListener() {
        adapter.setOnItemClickListener(new VideoRecyclerViewAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                File selectVideo = videos.get(position);
                L.e( "当前选中："+position );
                L.e( "本地视频个数："+videos.size() );
                L.e( "本地视频："+videos.toString() );
                String selectPath = selectVideo.getAbsolutePath();
                L.e("selectpath"+selectPath);
                //Intent intent = new Intent(getActivity(), PlayActivity.class);
                //intent.putExtra("selectVideo",selectPath);
                //startActivity(intent);
            }
        });
    }
    private void initData() {
        videos = new ArrayList<>();
        adapter= new VideoRecyclerViewAdapter(videos);
    }
    class ScannerAnsyTask extends AsyncTask<Void,Integer,List<File>> {

        @Override
        protected List<File> doInBackground(Void... params) {
            videos=getVideoFile(videos, Environment.getExternalStorageDirectory());
            //videos=filterVideo(videos);
            Message msg = new Message();
            msg.what = NO_VIDEOS;
            handler.sendMessage(msg);
            return videos;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<File> videoInfos) {
            super.onPostExecute(videoInfos);
        }

        /**
         * 获取视频文件
         *
         * @return
         */

        private List<File> getVideoFile(final List<File> list, File file) {

            file.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {

                    String name = file.getName();

                    int i = name.indexOf('.');
                    if (i != -1) {
                        name = name.substring(i);
                        if (name.equalsIgnoreCase(".mp4")
                                || name.equalsIgnoreCase(".3gp")
                                || name.equalsIgnoreCase(".wmv")
                                || name.equalsIgnoreCase(".ts")
                                || name.equalsIgnoreCase(".rmvb")
                                || name.equalsIgnoreCase(".mov")
                                || name.equalsIgnoreCase(".m4v")
                                || name.equalsIgnoreCase(".avi")
                                || name.equalsIgnoreCase(".mkv")
                                || name.equalsIgnoreCase(".flv")
                                || name.equalsIgnoreCase(".f4v")
                                || name.equalsIgnoreCase(".rm")
                                || name.equalsIgnoreCase(".mpg")
                                || name.equalsIgnoreCase(".swf")) {
                            videos.add(file);
                            return true;
                        }
                        //判断是不是目录
                    } else if (file.isDirectory()) {
                        getVideoFile(list, file);
                    }
                    return false;
                }
            });
            Message msg = new Message();
            msg.what = NEW_VIDEOS;
            Bundle bundle = new Bundle();
            msg.setData(bundle);
            handler.sendMessage(msg);
            return list;
        }

        /**10M=10485760 b,小于10m的过滤掉
         * 过滤视频文件
         * @param videoInfos
         * @return
         */
        private List<File> filterVideo(List<File> videoInfos){
            List<File> newVideos=new ArrayList<File>();
            for(File videoInfo:videoInfos){
                File f=new File(videoInfo.getPath());
                if(f.exists()&&f.isFile()&&f.length()>10485760*5){
                    newVideos.add(videoInfo);
                }else {
                }
            }
            return newVideos;
        }
    }
}
