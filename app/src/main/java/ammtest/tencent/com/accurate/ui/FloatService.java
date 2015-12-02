package ammtest.tencent.com.accurate.ui;

import android.app.Service;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import ammtest.tencent.com.accurate.R;
import ammtest.tencent.com.accurate.model.CaseChosenList;
import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseExcutorItem;
import ammtest.tencent.com.accurate.model.CaseExecutorModel;
import ammtest.tencent.com.accurate.model.CaseModel;
import ammtest.tencent.com.accurate.model.Config;
import ammtest.tencent.com.accurate.network.AccurateClient;
import ammtest.tencent.com.accurate.network.AmmHttpClient;
import ammtest.tencent.com.accurate.util.FileUtil;
import ammtest.tencent.com.accurate.util.StringUtil;
import cz.msebera.android.httpclient.Header;

public class FloatService extends Service {
    private String TAG = "AMMTEST.FLOATSERVICE";
    int caseId;
    LinearLayout mFloatLayout;
    LinearLayout mLy;
    WindowManager.LayoutParams wmParams;
    WindowManager wm;

    String resultFileName= null;
    int lastCaseId = -1;
    CaseModel mCaseModel;
    CaseChosenList mCaseChosenList;
    CaseExecutorModel mExcutirModel;

    private static Handler handler=new Handler();
    float pressedX,pressedY,x,y;
    private boolean hasStart = false;
    private int statusBarHeight = 0;
    private float xInScreen,yInScreen;
    private  float xInView, yInView;
    private float xDownInScreen, yDownInScreen;

    TextView nextTv;
    TextView titleTv;
    TextView firstInputTv;
    TextView otherInputTv;
    ImageView ctlImg;
    LinearLayout textLy;


    public FloatService() {
        super.onCreate();
        Log.i(TAG, "create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        mCaseModel = new CaseModel(getApplicationContext());
        mExcutirModel = new CaseExecutorModel(getApplicationContext());
        mCaseChosenList = CaseChosenList.getInstance();
        caseId = intent.getIntExtra(Constant.INTENT_CASE_ID, 0);
        creatFloatView();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        ctlImg = (ImageView)mFloatLayout.findViewById(R.id.id_control_img);
        nextTv = (TextView)mFloatLayout.findViewById(R.id.float_next);
        firstInputTv = (TextView)mFloatLayout.findViewById(R.id.float_first_input);
        titleTv = (TextView)mFloatLayout.findViewById(R.id.float_case_title);
        textLy = (LinearLayout)mFloatLayout.findViewById(R.id.float_text_ly);

        RelativeLayout retLy = (RelativeLayout)mFloatLayout.findViewById(R.id.float_relatvie);

        uiRenderByCase(mCaseModel.getCaseItem(caseId));
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT ;
        wmParams.width = (int) (rect.width()*0.8);
        wmParams.x = (int)(rect.width()*0.1);
        wmParams.y = rect.height()/4;

        wm.addView(mFloatLayout, wmParams);

        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiRenderByCase(mCaseChosenList.getNextCase(caseId));
            }
        });

        retLy.setOnTouchListener(new View.OnTouchListener() {
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
                        initialX = paramsF.x;
                        initialY = paramsF.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        //for double kill
                        // Get current time in nano seconds.
                        if(hasStart){
                            break;
                        }
                        long pressTime = System.currentTimeMillis();
                        if (pressTime-lastPressTime<500){

                            Intent intent = new Intent(FloatService.this, CaseDetailFloatService.class);
                            intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                            intent.putExtra(Constant.INTENT_CASE_FILENAME, resultFileName);
                            startService(intent);
                            finish();
                            lastPressTime = 0;
                        }else {
                            lastPressTime = pressTime;
                        }


                        break;
                    case MotionEvent.ACTION_MOVE:
                        //paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
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
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                String caseFilename = AccurateClient.getInstance().stopCase();
                                if(null == caseFilename){
                                    throw new Exception("unvalided return from wechat");
                                }
                                final File file = new File(Constant.CASE_DIR, caseFilename);
                                if(!file.exists()) {
                                    throw new Exception("no trace file");
                                }
                                lastCaseId = caseId;
                                resultFileName = file.getPath();
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
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ctlImg.setImageResource(R.drawable.start);
                                    nextTv.setEnabled(true);
                                    /*
                                    Intent intent = new Intent(FloatService.this, CaseDetailFloatService.class);
                                    intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                                    if(resultFile!=null){
                                        intent.putExtra(Constant.INTENT_CASE_FILENAME, resultFile.getPath());
                                    }
                                    startService(intent);
                                    finish();
                                    */
                                }
                            });
                        }
                    }).start();
                    hasStart = !hasStart;
                }else{
                    if(lastCaseId > 0 && resultFileName != null){
                        setCaseSuccess(lastCaseId, resultFileName);
                    }
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                AccurateClient.getInstance().startCase(String.valueOf(caseEntryItem.getCaseId()));
                                hasStart = !hasStart;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ctlImg.setImageResource(R.drawable.stop);
                                        nextTv.setEnabled(false);
                                        textLy.setEnabled(false);
                                        firstInputTv.setSingleLine(false);
                                        //input 一段时间后自动消失
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                firstInputTv.setSingleLine(true);
                                            }
                                        }, Config.CASE_FLOAT_TOTAL_INPUT_LAST);
                                    }
                                });
                            } catch (final IOException e) {
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

            }

        });
    }

    private void uiRenderByCase(CaseEntryItem caseEntryItem){
        if (caseEntryItem == null){
            //no next, back to module case list
            finish();
            Intent intent = new Intent(FloatService.this, ModuleCasesActivity.class);
           // intent.putExtra(Constant.INTENT_MODULE_NAME, this.);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        caseId = caseEntryItem.getCaseId();
        titleTv.setText(caseEntryItem.getCaseName());
        String input = caseEntryItem.getCaseInput();
        TextView caseIdTv = (TextView)mFloatLayout.findViewById(R.id.case_id_tv);
        caseIdTv.setText(StringUtil.caseIdToStr(caseEntryItem.getCaseId()));
        firstInputTv.setSingleLine(false);
        firstInputTv.setText(input);

    }

    private void finish() {
        if(mFloatLayout != null){
            wm.removeView(mFloatLayout);
        }
        if(resultFileName != null){
            resultFileName = null;
        }
        stopSelf();
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
        Log.i(TAG, "sttiusBar:" + statusBarHeight);
        return statusBarHeight;
    }

    private  void setCaseSuccess(int caseId, String filename){
        CaseExcutorItem excutorItem = new CaseExcutorItem();
        excutorItem.setExcuteStatus(CaseExcutorItem.EXCUTE_STATUS_SUCCESS);
        mExcutirModel.insert(excutorItem);
        uploadResult(filename, false);
    }

    private void uploadResult(final String filename, boolean isSync){
        File file = new File(filename);
        RequestParams params = new RequestParams();
        try {
            params.put("caseresult", file);
        } catch (FileNotFoundException e) {
            Toast.makeText(FloatService.this, "unvalid path"+filename, Toast.LENGTH_SHORT).show();
        }
        AmmHttpClient.post("uploadResult.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Log.e(TAG, "failed:" + s);
                FileUtil.deleteFile(filename);
                Toast.makeText(FloatService.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                Log.i(TAG, s);
                Toast.makeText(FloatService.this, s, Toast.LENGTH_SHORT).show();
            }
        }, isSync);
    }
}
