package dec.genius.convert.utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Time;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 
 * @ClassName: FileUtil 
 * @Description: 文件辅助类
 * @author WangFusheng 
 * @date 2015年9月1日 上午10:59:32
 */
public class FileUtil {
    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * 获取文件夹内文件数量
     *
     * @param path
     * @return
     */
    public static long getFileCount(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1;
        }
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * 获取文件夹大小
     *
     * @param file
     * @return
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getFilePtah(Context context) {
        String sdCardPath = null;
        if (SDCardUtils.hasSDCard() && !SDCardUtils.isSDCardEnable())
            sdCardPath = context.getExternalCacheDir().toString();
        else
            sdCardPath = context.getCacheDir().toString();
        return sdCardPath;
    }

    public static File getCameraPath(Context context) {
        File picture = null;
        String sdCardPath = Environment.getExternalStorageDirectory().toString();
        try {
            String fodlerPath = createPath(sdCardPath + File.separator + AppUtils.getAppName(context));
            String photoName = getPhotoName("jpg");
            picture = new File(fodlerPath + File.separator + photoName);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "相机路径初始化失败，请重新再试", Toast.LENGTH_SHORT).show();
            return null;
        }
        return picture;
    }

    @SuppressWarnings("deprecation")
	public static String getPhotoName(String imgType) {
        String mCurrentSysTime = null;
        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        int minute = time.minute;
        int hour = time.hour;
        int sec = time.second;
        mCurrentSysTime = year + "" + month + "" + day + "" + hour + "" + minute + "" + sec + "." + imgType;
        return mCurrentSysTime;
    }

    public static File getImageLoaderCachePath(Context context) {
        File file = null;
        file = new File(getFilePtah(context) + File.separator + "UIImage");
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    /**
     * 转换文件大小
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0.0M";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    /**
	 * 获取文件大小
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size = 0;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param size
	 *            字节
	 * @return
	 */
	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		DecimalFormat df = new DecimalFormat("##.##");
		float temp = (float) size / 1024;
		if (temp >= 1024) {
			return df.format(temp / 1024) + "M";
		} else {
			return df.format(temp) + "K";
		}
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 获取目录文件大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}

	/**
	 * 获取目录文件个数
	 * 
	 * @param f
	 * @return
	 */
	public long getFileList(File dir) {
		long count = 0;
		File[] files = dir.listFiles();
		count = files.length;
		for (File file : files) {
			if (file.isDirectory()) {
				count = count + getFileList(file);// 递归
				count--;
			}
		}
		return count;
	}

	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}
		byte buffer[] = out.toByteArray();
		out.close();
		return buffer;
	}

    /*
     * 创建目录
     */
    public static String createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }
    
    public static String getRootFilePath() {
		if (SDCardUtils.hasSDCard()&&!SDCardUtils.isSDCardEnable()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
		}
	}
    /**
     * 
     * @Title: getCacheFileSavePath 
     * @Description: 生成缓存文件目录
     * @author WangFusheng 
     * @param @param appName
     * @param @return    
     * @return String    
     * @throws
     */
    public static String getCacheFileSavePath(String appName) {
		return getRootFilePath() + appName+"/cache/";
	}
	/**
	 * 
	 * @Title: getDownloadFileSavePath 
	 * @Description: 生成下载文件目录
	 * @author WangFusheng 
	 * @param @param appName
	 * @param @return    
	 * @return String    
	 * @throws
	 */
	public static String getDownloadFileSavePath(String appName) {
			return getRootFilePath() + appName+"/files/";
	}
	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (TextUtils.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 根据文件的绝对路径获取文件名但不包含扩展名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameNoFormat(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
				point);
	}
	
	/**
	 * 获取文件扩展名
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (TextUtils.isEmpty(fileName))
			return "";
		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}
	/**
	 * 使用当前时间戳拼接一个唯一的文件名
	 * @param format
	 * @return
	 */
    @SuppressLint("SimpleDateFormat")
	public static String getTempFileName() 
    {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
    	String fileName = format.format( new Timestamp( System.currentTimeMillis()) );
    	return fileName;
    }
	/**
	 * 检查SD卡根目录下该文件是否存在
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String name) {
		boolean status;
		if (!name.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + name);
			status = newPath.exists();
		} else {
			status = false;
		}
		return status;

	}
}
