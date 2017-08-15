package dec.genius.convert.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import dec.genius.convert.R;
import dec.genius.convert.base.MyApplication;
import dec.genius.convert.bean.Song;

/**
 * Created by Administrator on 2017/5/17 0017.
 */

public class AudioRecyclerViewAdapter extends RecyclerView.Adapter<AudioRecyclerViewAdapter.MyViewHolder> {
    int pos ;
    List<Song> songs;
    public static final String TAG = "AudioewAdapter";
    onRecyclerViewItemClickListener itemClickListener;
    public AudioRecyclerViewAdapter(List<Song> songs){
        this.songs = songs;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                MyApplication.getInstance()).inflate(R.layout.audio_recyclerview_item, parent,
                false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    //注意这里使用getTag方法获取position
                    itemClickListener.onItemClick(v,(int)v.getTag());
                }
            }
        });
        return new AudioRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        pos = position;
        Log.e(TAG, "谁先执行"+position );
        holder.tv.setText(songs.get(position).getFileName());
        holder.container.setTag(position);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
    public void setOnItemClickListener(AudioRecyclerViewAdapter.onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
        //  Log.d("ddd", itemClickListener.toString());
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;
        RelativeLayout container;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.fragment_audio_item_tv);
            iv = (ImageView) itemView.findViewById(R.id.fragment_audio_item_iv);
            container = (RelativeLayout) itemView.findViewById(R.id.fragment_audio_item_container);
        }
    }
    public  interface onRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }
}
