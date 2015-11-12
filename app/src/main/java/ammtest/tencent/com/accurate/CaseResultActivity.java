package ammtest.tencent.com.accurate;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseModel;

public class CaseResultActivity extends Activity {
    private  int caseId;
    private CaseEntryItem caseEntryItem;
    private Map<Long, MethodItem> caseResult = new HashMap<Long, MethodItem>();
    static final int HANDLER_STATISTIC = 1;
    static final int HANDLER_DETAIL = 2;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case  HANDLER_STATISTIC:
                    break;
                default:
                    ;
            }
            super.handleMessage(msg);
        }
    };
    public static class MethodItem{
        long threadId;
        String threadName;
        long methodId;

        public MethodItem(long methodId, String threadName, long threadId) {
            this.methodId = methodId;
            this.threadName = threadName;
            this.threadId = threadId;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_result);
        Intent intent = getIntent();
        intent.getIntExtra(Constant.INTENT_CASE_ID, 0);
        caseEntryItem = CaseModel.getInstance().getCaseItem(caseId);

        new Thread(){

            @Override
            public void run() {
                String caseName = caseEntryItem.getCaseName();
                //find class files
            }
        }.start();

    }

}
