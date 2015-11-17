package ammtest.tencent.com.accurate;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
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

import org.w3c.dom.Text;

import java.io.IOException;

import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseModel;
import ammtest.tencent.com.accurate.network.AccurateClient;

public class CaseDetailFloatService extends Service {
    private WindowManager.LayoutParams wmParams;
    private WindowManager wm;
    private LinearLayout mFloatLayout;
    private String TAG = "ammtest.CaseDetailFloatService";
    private String caseFileName = null;
    int caseId;
    public CaseDetailFloatService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        caseId = intent.getIntExtra(Constant.INTENT_CASE_ID, 0);
        caseFileName = intent.getStringExtra(Constant.INTENT_CASE_FILENAME);
        creatFloatView();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatLayout != null){
            wm.removeView(mFloatLayout);
        }
        mFloatLayout = null;
    }

    private void finish(){
        if(mFloatLayout != null){
            wm.removeView(mFloatLayout);
        }
        mFloatLayout = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void creatFloatView(){
        final CaseEntryItem caseEntryItem = new CaseModel(getApplicationContext()).getCaseItem(caseId);
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
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = (int) (rect.width() * 0.05);
        wmParams.y = (int) (rect.height() * 0.2);
        wmParams.height = (int) (rect.height() * 0.6);
        wmParams.width = (int) (rect.width() * 0.9);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout)inflater.inflate(R.layout.case_detail_float, null);
        wm.addView(mFloatLayout, wmParams);
        LinearLayout mLy = (LinearLayout) mFloatLayout.findViewById(R.id.case_detail_float_ly);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mLy.getMeasuredWidth() / 2);
        Log.i(TAG, "Height/2--->" + mLy.getMeasuredHeight() / 2);

        TextView stopBtn = (TextView)mFloatLayout.findViewById(R.id.case_float_stop_btn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(CaseDetailFloatService.this, MainActivity.class);
                intent.putExtra(Constant.INTENT_CASE_REFRESH, false);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Button checkBtn = (Button)mFloatLayout.findViewById(R.id.case_check_btn);
        if(caseFileName != null){
            checkBtn.setVisibility(View.VISIBLE);
            checkBtn.setEnabled(true);
            checkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CaseDetailFloatService.this, CaseResultActivity.class);
                    intent.putExtra(Constant.INTENT_CASE_FILENAME, caseFileName);
                    intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }else {
            checkBtn.setEnabled(true);
        }


        Button startBtn = (Button)mFloatLayout.findViewById(R.id.case_float_start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseDetailFloatService.this, FloatService.class);
                intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                startService(intent);
                finish();
            }
        });
        TextView caseTv = (TextView)mFloatLayout.findViewById(R.id.case_defail_tv);
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2>"+caseEntryItem.getCaseName()+"</h2>");


        if(null != caseEntryItem.getCaseModule()){
            sb.append("<span class='name'>模块:</span>"+
                    "<span class='content'>"+wrapContent(caseEntryItem.getCaseModule())+"</span><br>");
        }

        if(null != caseEntryItem.getCaseInput()){
            sb.append("<span class='name'>输入:</span>"+
                    "<span class='content'>"+wrapContent(caseEntryItem.getCaseInput())+"</span><br>");
        }

        if(null != caseEntryItem.getCaseOutput()){
            sb.append("<span class='name'>输出:</span>"+
                    "<span class='content'>"+wrapContent(caseEntryItem.getCaseOutput())+"</span><br>");
        }

        if(null != caseEntryItem.getCaseCheckList()){
            sb.append("<span class='name'>检查点:</span>"+
                    "<span class='content'>"+wrapContent(caseEntryItem.getCaseCheckList())+"</span><br>");
        }

        sb.append("</body></html>");
        Log.i(TAG, sb.toString());
        caseTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        caseTv.setText(Html.fromHtml(sb.toString()));
        /*
        TextView caseTitle = (TextView)mFloatLayout.findViewById(R.id.case_float_case_title);
        caseTitle.setText(caseEntryItem.getCaseName());
        TextView caseInput = (TextView)mFloatLayout.findViewById(R.id.case_float_input);
        caseInput.setText(caseEntryItem.getCaseInput());
        TextView caseOutput = (TextView)mFloatLayout.findViewById(R.id.case_float_output);
        caseOutput.setText(caseEntryItem.getCaseOutput());
        TextView caseCheck = (TextView)mFloatLayout.findViewById(R.id.case_float_case_check);
        caseCheck.setText(caseEntryItem.getCaseCheckList());
        */
    }

    private String wrapContent(String content){
        String result = content.replace("\r\n", "<br>");
        result = result.replace("\n", "<br>");
        return result;
    }
}
