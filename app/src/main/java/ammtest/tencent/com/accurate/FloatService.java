package ammtest.tencent.com.accurate;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseModel;
import ammtest.tencent.com.accurate.network.AccurateClient;
import ammtest.tencent.com.accurate.network.AmmHttpClient;
import cz.msebera.android.httpclient.Header;

public class FloatService extends Service {
    private String TAG = "AMMTEST.FLOATSERVICE";
    int caseId;
    LinearLayout mFloatLayout;
    LinearLayout mLy;
    WindowManager.LayoutParams wmParams;
    WindowManager wm;
    TextView ctlText;
    ImageView ctlImg;
    File resultFile = null;
    private static Handler handler=new Handler();
    float pressedX,pressedY,x,y;
    private boolean hasStart = false;
    private int statusBarHeight = 0;
    private float xInScreen,yInScreen;
    private  float xInView, yInView;
    private float xDownInScreen, yDownInScreen;


    public FloatService() {
        super.onCreate();
        Log.i(TAG, "create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        caseId = intent.getIntExtra(Constant.INTENT_CASE_ID, 0);
        creatFloatView();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatLayout != null){
            wm.removeView(mFloatLayout);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }

    public void creatFloatView(){
        final CaseEntryItem caseEntryItem = new CaseModel(FloatService.this).getCaseItem(caseId);// CaseModel.getInstance().getCaseItem(caseId);
        Log.d(TAG, caseEntryItem.toString());

        wm = (WindowManager)FloatService.this.getSystemService(getApplication().WINDOW_SERVICE);

        Display d = wm.getDefaultDisplay();
        Rect rect = new Rect();
        d.getRectSize(rect);
        Log.i(TAG, rect.toShortString());

        wmParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        wmParams.gravity = Gravity.TOP | Gravity.LEFT;

        /*wmParams = new WindowManager.LayoutParams();

        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        */

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout)inflater.inflate(R.layout.float_view, null);


        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));



        mLy = (LinearLayout)mFloatLayout.findViewById(R.id.id_ly_main);
        ctlText = (TextView)mFloatLayout.findViewById(R.id.id_control_text);
        ctlText.setText(R.string.case_run);
        ctlImg = (ImageView)mFloatLayout.findViewById(R.id.id_control_img);

        wmParams.x = rect.width() - mFloatLayout.getMeasuredWidth();
        wmParams.y = rect.height()/2;

        wm.addView(mFloatLayout, wmParams);

        ctlImg.setOnTouchListener(new View.OnTouchListener() {
            private WindowManager.LayoutParams paramsF = wmParams;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private long lastPressTime =0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Get current time in nano seconds.
                        long pressTime = System.currentTimeMillis();



                        lastPressTime = pressTime;
                        initialX = paramsF.x;
                        initialY = paramsF.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                        paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                        wm.updateViewLayout(mFloatLayout, paramsF);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                //prevent Click event
                return false;
            }
        });

        ctlImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasStart){
                    ctlImg.setImageResource(R.drawable.start);
                    ctlText.setText(R.string.case_run);
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                String response = AccurateClient.getInstance().stopCase();
                                String [] res = response.split(":");
                                final String caseFilename;
                                if(res.length ==2){
                                    caseFilename = res[1].trim();
                                }else{
                                    throw new Exception("unvalided return from wechat");
                                }
                                final File file = new File("/sdcard/mmtest/accurate", caseFilename);
                                if(!file.exists()) {
                                    throw new Exception("no trace file");
                                }
                                resultFile = file;
                            } catch (final Exception e) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String errmsg = getResources().getString(R.string.case_stop_errmsg)+e.getMessage();
                                        Log.e(TAG, errmsg);
                                        Toast.makeText(FloatService.this, errmsg, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(FloatService.this, CaseDetailFloatService.class);
                                        intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                                        startService(intent);
                                        finish();
                                    }
                                });
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(FloatService.this, CaseDetailFloatService.class);
                                    intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                                    if(resultFile!=null){
                                        intent.putExtra(Constant.INTENT_CASE_FILENAME, resultFile.getPath());
                                    }
                                    startService(intent);
                                    finish();
                                }
                            });
                        }
                    }).start();
                }else{
                    ctlImg.setImageResource(R.drawable.stop);
                    ctlText.setText(R.string.case_stop);
                    new Thread(new Runnable(){
                        @Override
                        public void run() {

                            try {
                                AccurateClient.getInstance().startCase(String.valueOf(caseEntryItem.getCaseId()));
                            } catch (final Exception e) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String errmsg = getResources().getString(R.string.case_stop_errmsg)+e.getMessage();
                                        Log.e(TAG, errmsg);
                                        Toast.makeText(FloatService.this, errmsg, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                e.printStackTrace();
                            }
                        }

                    }).start();
                }
                hasStart = !hasStart;
            }

        });
    }

    private void finish() {
        if(mFloatLayout != null){
            wm.removeView(mFloatLayout);
        }
        mFloatLayout = null;
        if(resultFile != null){
            resultFile = null;
        }
    }

    private  void updateViewPosition(){
        wmParams.x = (int) (xInScreen - xInView);
        wmParams.y = (int) (yInScreen - yInView);
        Log.i(TAG, "wm.x:"+wmParams.x+",wm.y:"+wmParams.y);
        wm.updateViewLayout(mFloatLayout, wmParams);
    }

    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "sttiusBar:"+statusBarHeight);
        return statusBarHeight;
    }
}
