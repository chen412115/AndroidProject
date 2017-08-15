package dec.genius.convert.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import dec.genius.convert.R;
import dec.genius.convert.base.MyApplication;
import dec.genius.convert.ui.fragment.PictureFragment;

/**
 * Created by Administrator on 2017/5/17 0017.
 */

public class PicRecyclerViewAdapter extends RecyclerView.Adapter<PicRecyclerViewAdapter.MyViewHolder>{
    List<PictureFragment.Pic> pics;
    int pos;
    public static final String TAG = "PicRewAdapter";
    onRecyclerViewItemClickListener itemClickListener;
    public PicRecyclerViewAdapter(List<PictureFragment.Pic> pics){
        this.pics = pics;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                MyApplication.getInstance()).inflate(R.layout.pic_recyclerview_item, parent,
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
        Log.e(TAG, "onBindViewHolder: "+pics.get(position) );
        holder.tv.setText(pics.get(position).getName());
        Glide.with(MyApplication.getInstance()).load(pics.get(position).getPath()).asBitmap().override(200,140).centerCrop().skipMemoryCache(false)
                .into(holder.iv);
        holder.container.setTag(position);
    }

    @Override
    public int getItemCount() {
        return pics.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;
        RelativeLayout container;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.fragment_pic_item_tv);
            iv = (ImageView) itemView.findViewById(R.id.fragment_pic_item_iv);
            container = (RelativeLayout) itemView.findViewById(R.id.fragment_pic_item_container);
        }
    }
    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
        //  Log.d("ddd", itemClickListener.toString());
    }

    public  interface onRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }
}
