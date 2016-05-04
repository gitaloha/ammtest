package ammtest.tencent.com.accurate.ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.IBinder;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;

import ammtest.tencent.com.accurate.R;
import ammtest.tencent.com.accurate.model.CaseChosenList;
import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseModel;
import ammtest.tencent.com.accurate.network.AmmHttpClient;
import ammtest.tencent.com.accurate.util.FileUtil;
import ammtest.tencent.com.accurate.util.StringUtil;
import cz.msebera.android.httpclient.Header;

public class CaseDetailFloatService extends Service {
    private WindowManager.LayoutParams wmParams;
    private WindowManager wm;
    private LinearLayout mFloatLayout;
    private String TAG = "ammtest.CaseDetailFloatService";
    private String caseFileName = null;
    int caseId;
    private GestureDetector gestureDetector;
    CaseChosenList caseList ;
    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 120) {
                        CaseEntryItem caseEntry = caseList.getLastCase(caseId);
                        changeToCase(caseEntry);
                    } else if (x < -120) {
                        CaseEntryItem caseEntry = caseList.getNextCase(caseId);
                        changeToCase(caseEntry);

                    }
                    return true;
                }
            };
    public CaseDetailFloatService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        caseList = CaseChosenList.getInstance();
        caseId = intent.getIntExtra(Constant.INTENT_CASE_ID, 0);
        caseFileName = intent.getStringExtra(Constant.INTENT_CASE_FILENAME);
        gestureDetector = new GestureDetector(CaseDetailFloatService.this,onGestureListener);
        creatFloatView();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void finish(){
        if(mFloatLayout != null){
            wm.removeView(mFloatLayout);
        }
        stopSelf();
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

        TextView backFloatBtn = (TextView)mFloatLayout.findViewById(R.id.case_float_back_float_btn);
        backFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseDetailFloatService.this, FloatService.class);
                intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
                finish();

            }
        });

        Button backLaunch = (Button)mFloatLayout.findViewById(R.id.case_float_back_btn);
        backLaunch.setText(R.string.case_float_main_menu);
        backLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(CaseDetailFloatService.this, LaunchActivity.class);
                intent.putExtra(Constant.INTENT_CASE_REFRESH, false);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        Button checkBtn = (Button)mFloatLayout.findViewById(R.id.case_check_btn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caseFileName == null){
                    Log.e(TAG, "checkBtn caseFileName = null");
                    return;
                }
                Intent intent = new Intent(CaseDetailFloatService.this, CaseResultActivity.class);
                intent.putExtra(Constant.INTENT_CASE_FILENAME, caseFileName);
                intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        Button failedBtn = (Button)mFloatLayout.findViewById(R.id.case_float_failed_btn);
        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.deleteFile(caseFileName);
                Intent intent = new Intent(CaseDetailFloatService.this, FloatService.class);
                intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
                finish();
            }
        });



        TextView caseTv = (TextView)mFloatLayout.findViewById(R.id.case_defail_tv);
        caseTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        renderContent(caseEntryItem);
    }

    private void changeToCase(CaseEntryItem caseEntry){
        if (caseEntry == null){
            //no next
            finish();
            Intent intent = new Intent(CaseDetailFloatService.this, ModuleCasesActivity.class);
            intent.putExtra(Constant.INTENT_CASE_FILENAME, caseFileName);
            intent.putExtra(Constant.INTENT_CASE_ID, caseId);
            intent.putExtra(Constant.INTENT_CASE_REFRESH, false);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        caseId = caseEntry.getCaseId();
        //upload
        setNotRan();
        renderContent(caseEntry);
    }

    private void renderContent(CaseEntryItem caseEntryItem ){
        if(mFloatLayout == null){
            return;
        }

        Button checkBtn = (Button)mFloatLayout.findViewById(R.id.case_check_btn);
        Button failedBtn = (Button)mFloatLayout.findViewById(R.id.case_float_failed_btn);
        if(hasRan()){
            checkBtn.setEnabled(true);
            failedBtn.setEnabled(true);
        }else{
            checkBtn.setEnabled(false);
            failedBtn.setEnabled(false);
        }


        TextView caseTv = (TextView)mFloatLayout.findViewById(R.id.case_defail_tv);
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h2>Id: "+StringUtil.caseIdToStr(caseId)+caseEntryItem.getCaseName()+"</h2>");


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

        TextView pageTv = (TextView)mFloatLayout.findViewById(R.id.case_page);
        int index = CaseChosenList.getInstance().getIndex(caseId);
        int count = CaseChosenList.getInstance().getCount();
        pageTv.setText(String.format("%d/%d", index, count));

        //TextView caseIdTv = (TextView)mFloatLayout.findViewById(R.id.case_id_tv);
        //caseIdTv.setText(StringUtil.caseIdToStr(caseId));
    }

    private String wrapContent(String content){
        String result = content.replace("\r\n", "<br>");
        result = result.replace("\n", "<br>");
        return result;
    }

    private boolean hasRan(){
        return caseFileName != null;
    }

    private void setNotRan(){
        caseFileName = null;
    }

    private void uploadResult(String filename, boolean isSync){
        File file = new File(filename);
        RequestParams params = new RequestParams();
        try {
            params.put("caseresult", file);
        } catch (FileNotFoundException e) {
            Toast.makeText(CaseDetailFloatService.this, "unvalid path"+caseFileName, Toast.LENGTH_SHORT).show();
        }
        AmmHttpClient.post("uploadResult.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Log.e(TAG, s);
                Toast.makeText(CaseDetailFloatService.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                Log.i(TAG, s);
                Toast.makeText(CaseDetailFloatService.this, s, Toast.LENGTH_SHORT).show();
            }
        }, isSync);
    }
}
