package dec.genius.convert.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * 1.获取视频的缩略图
 */

public class VideoUtils {
    static String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";

    public static String getAppDir(){
        String appPath = path+"Genius";
        File file = new File(appPath);
        if (!file.exists()){
            file.mkdirs();
        }
        return appPath;
    }

    public static Bitmap getThumb(String video) {
        if (video == null) return null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        return ThumbnailUtils.createVideoThumbnail(video, MediaStore.Images.Thumbnails.MINI_KIND);
    }
}
