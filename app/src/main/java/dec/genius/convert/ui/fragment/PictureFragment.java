package dec.genius.convert.ui.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.List;

import dec.genius.convert.R;
import dec.genius.convert.base.BaseFragment;
import dec.genius.convert.ui.adapter.PicRecyclerViewAdapter;
import dec.genius.convert.utils.L;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_picture)
public class PictureFragment extends BaseFragment {
    @ViewInject(R.id.pic_recyclerView)
    RecyclerView recyclerView;
    List<Pic> pics;
    PicRecyclerViewAdapter adapter;

    public PictureFragment() {
        // Required empty public constructor
    }


    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        getPics(); //   先获取图片
        initView();
        initData();
        initListener();
        recyclerView.setAdapter(adapter);
    }
    private void initListener() {
        adapter.setOnItemClickListener(new PicRecyclerViewAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initData() {
        adapter= new PicRecyclerViewAdapter(pics);
    }
    private void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4)); // 设置样式
        recyclerView.setItemAnimator(new DefaultItemAnimator()); //动画有四种，分别是移动、改变、添加、删除
        recyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL)); //添加分割线
    }
    public void getPics(){
        pics = new ArrayList<>();
        setPermissions();
    }
    static final String[] PERMISSION = new String[]{
            Manifest.permission.READ_CONTACTS,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.WRITE_CALL_LOG,        //读取设备信息
    };
    private void setPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Android 6.0申请权限
            ActivityCompat.requestPermissions(getActivity(),PERMISSION,1);
        }else{
            L.i("权限申请ok");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //获取图片的名称
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //获取图片绝对路径
            String desc = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            pics.add(new Pic(name,desc));
        }
    }

    public class Pic{
        String name ;
        String path;
        public Pic(String name,String path){
            this.name = name ;
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return "Pic{" +
                    "name='" + name + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }
}
