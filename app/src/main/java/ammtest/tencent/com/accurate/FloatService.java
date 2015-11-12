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

import java.io.IOException;

import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseModel;
import ammtest.tencent.com.accurate.network.AccurateClient;

public class FloatService extends Service {
    private String TAG = "AMMTEST.FLOATSERVICE";
    int caseId;
    LinearLayout mFloatLayout;
    LinearLayout mLy;
    WindowManager.LayoutParams wmParams;
    WindowManager wm;
    TextView ctlText;
    ImageView ctlImg;
    private static Handler handler=new Handler();
    float mTouchStartX,mTouchStartY,x,y;
    private boolean hasStart;


    public FloatService() {
        super.onCreate();
        Log.i(TAG, "create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        creatFloatView();
        caseId = intent.getIntExtra(Constant.INTENT_CASE_ID, 0);
        return super.onStartCommand(intent, flags, startId);
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
        final CaseEntryItem caseEntryItem = new CaseEntryItem(getResources().getString(R.string.app_name));// CaseModel.getInstance().getCaseItem(caseId);
        wmParams = new WindowManager.LayoutParams();
        Context context = getApplication();
        if(context == null){
            Log.i(TAG, "is null");
            return;
        }
        wm = (WindowManager)FloatService.this.getSystemService(Context.WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager--->" + wm);
        Display d = wm.getDefaultDisplay();
        Rect rect = new Rect();
        d.getRectSize(rect);
        Log.i(TAG, d.toString());
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = (int) (rect.height() * 0.1);
        wmParams.y = (int) (rect.width() * 0.1);
        wmParams.height = (int) (rect.height() * 0.8);
        wmParams.width = (int) (rect.width() * 0.8);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout)inflater.inflate(R.layout.float_view, null);
        wm.addView(mFloatLayout, wmParams);
        mLy = (LinearLayout)mFloatLayout.findViewById(R.id.id_ly_main);
        ctlText = (TextView)mFloatLayout.findViewById(R.id.id_control_text);
        ctlText.setText(caseEntryItem.getCaseName());
        ctlImg = (ImageView)mFloatLayout.findViewById(R.id.id_control_img);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mLy.getMeasuredWidth()/2);
        Log.i(TAG, "Height/2--->" + mLy.getMeasuredHeight()/2);

        ctlImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getRawX();
                y = event.getRawY();

                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchStartX =  event.getX();
                        mTouchStartY =  event.getY();
                        Log.i("startP", "startX"+mTouchStartX+"====startY"+mTouchStartY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updateViewPosition();
                        break;

                    case MotionEvent.ACTION_UP:
                        updateViewPosition();
                        mTouchStartX=mTouchStartY=0;
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
                                AccurateClient.getInstance().stopCase();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ctlImg.setImageResource(R.drawable.start);
                                    }
                                });
                            } catch (IOException e) {
                                Toast.makeText(FloatService.this, R.string.case_stop_errmsg, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                    }).start();

                    Intent intent = new Intent(FloatService.this, CaseActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                    startActivity(intent);
                    wm.removeView(mFloatLayout);
                }else{
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                AccurateClient.getInstance().startCase(caseEntryItem.getCaseName());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ctlImg.setImageResource(R.drawable.stop);
                                    }
                                });
                            } catch (IOException e) {
                                Toast.makeText(FloatService.this, R.string.case_start_errmsg, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                    }).start();



                }
                hasStart = !hasStart;
            }
        });
    }

    private  void updateViewPosition(){
        wmParams.x=(int)( x-mTouchStartX);
        wmParams.y=(int) (y-mTouchStartY);
        wm.updateViewLayout(mFloatLayout, wmParams);
    }
}
