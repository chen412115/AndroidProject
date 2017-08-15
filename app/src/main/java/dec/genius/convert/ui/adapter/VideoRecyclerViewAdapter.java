package dec.genius.convert.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;



import java.io.File;
import java.util.List;

import dec.genius.convert.R;
import dec.genius.convert.base.MyApplication;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.MyViewHolder> {
    List<File> files;
    int pos;
    public static final String TAG = "VideoRecyclerVi";
    onRecyclerViewItemClickListener itemClickListener;
    public VideoRecyclerViewAdapter(List<File> files){
        this.files = files;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                MyApplication.getInstance()).inflate(R.layout.video_recyclerview_item, parent,
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
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        pos = position;
        Log.e(TAG, "onBindViewHolder: 谁先执行"+position );
        holder.tv.setText(files.get(position).getName());
        Glide.with(MyApplication.getInstance()).load(Uri.fromFile(files.get(position))).asBitmap().override(200,140).centerCrop().skipMemoryCache(false)
                .into(holder.iv);
        holder.container.setTag(position);
        //holder.iv.setImageBitmap(getThumb(files.get(position).getAbsolutePath()));

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        RelativeLayout container;
        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.fragment_video_item_tv);
            iv = (ImageView) view.findViewById(R.id.fragment_video_item_iv);
            container = (RelativeLayout) view.findViewById(R.id.fragment_video_item_container);
        }
    }
    //获取视频缩略图
    public static Bitmap getThumb(String video) {
        if (video == null) return null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        return ThumbnailUtils.createVideoThumbnail(video, MediaStore.Images.Thumbnails.MINI_KIND);
    }

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
        //  Log.d("ddd", itemClickListener.toString());
    }
    //点击时间
    public  interface onRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }
}
