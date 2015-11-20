package ammtest.tencent.com.accurate.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by xiazeng on 2015/11/13.
 */
public class AmmHttpClient {
    private static final String BASE_URL = "http://128.199.109.248/";

    private static AsyncHttpClient asyncClient = new AsyncHttpClient();
    private static SyncHttpClient syncClient = new SyncHttpClient();



    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, boolean isSync) {
        if(isSync){
            syncClient.get(getAbsoluteUrl(url), params, responseHandler);
        }else {
            asyncClient.get(getAbsoluteUrl(url), params, responseHandler);
        }

    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, boolean isSync) {
        if(isSync){
            syncClient.post(getAbsoluteUrl(url), params, responseHandler);
        }else{
            asyncClient.post(getAbsoluteUrl(url), params, responseHandler);
        }

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
