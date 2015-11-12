package ammtest.tencent.com.accurate;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.network.AccurateClient;

public class CaseDetailFloatService extends Service {
    private WindowManager.LayoutParams wmParams;
    private WindowManager wm;
    private LinearLayout mFloatLayout;
    private String TAG = "ammtest.CaseDetailFloatService";

    public CaseDetailFloatService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        creatFloatView();
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
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void creatFloatView(){
        final CaseEntryItem caseEntryItem = new CaseEntryItem(getResources().getString(R.string.app_name));// CaseModel.getInstance().getCaseItem(caseId);
        wmParams = new WindowManager.LayoutParams();
        Context context = getApplication();
        if(context == null){
            Log.i(TAG, "is null");
            return;
        }
        wm = (WindowManager)CaseDetailFloatService.this.getSystemService(Context.WINDOW_SERVICE);
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
        mFloatLayout = (LinearLayout)inflater.inflate(R.layout.case_detail_float, null);
        wm.addView(mFloatLayout, wmParams);
        LinearLayout mLy = (LinearLayout) mFloatLayout.findViewById(R.id.case_detail_float_ly);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mLy.getMeasuredWidth()/2);
        Log.i(TAG, "Height/2--->" + mLy.getMeasuredHeight() / 2);

        Button stopBtn = (Button)mFloatLayout.findViewById(R.id.case_float_stop_btn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(mFloatLayout);
            }
        });


    }
}
