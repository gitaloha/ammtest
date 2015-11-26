package ammtest.tencent.com.accurate.util;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by xiazeng on 2015/11/12.
 */
public class FileUtil {
    static final String TAG = "ammtest.FileUtil";
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean deleteFile(String filename){
        if(filename != null){
            File file = new File(filename);
            boolean result = file.delete();
            if(result){
                Log.d(TAG, "delete " + filename + " successfully");
            }else{
                Log.d(TAG, "can not delete "+filename);
            }
            return result;
        }
        else{
            Log.d(TAG, "caseFileName is null ");
            return false;
        }
    }
}
