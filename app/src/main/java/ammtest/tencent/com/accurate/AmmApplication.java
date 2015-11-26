package ammtest.tencent.com.accurate;

import android.app.Application;

import ammtest.tencent.com.accurate.crash.CrashHandler;

/**
 * Created by xiazeng on 2015/11/25.
 */
public class AmmApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //CrashHandler crashHandler = CrashHandler.getInstance();
        //crashHandler.init(getApplicationContext());
    }
}
