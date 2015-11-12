package ammtest.tencent.com.accurate.util;

import android.os.Environment;

/**
 * Created by xiazeng on 2015/11/12.
 */
public class FileUtil {
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
