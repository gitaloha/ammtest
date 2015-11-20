package ammtest.tencent.com.accurate.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ammtest.tencent.com.accurate.R;
import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseModel;


public class CaseActivity extends Activity {
    int caseId;
    String caseName;
    private String TAG = "ammtet.CaseActivity";

    void initView(){
        Intent intent = getIntent();
        caseId = intent.getIntExtra(Constant.INTENT_CASE_ID, 0);
        CaseEntryItem entryItem =new CaseModel(CaseActivity.this.getApplicationContext()).getCaseItem(caseId);
        TextView titleTv = (TextView)findViewById(R.id.case_title);
        titleTv.setText(String.format("【%d】%s", caseId, entryItem.getCaseName()));
        TextView contentTx = (TextView)findViewById(R.id.case_content);
        contentTx.setText(entryItem.getCaseModule());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);
        initView();
        Button checkBtn = (Button)findViewById(R.id.case_check_btn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseActivity.this, CaseResultActivity.class);
                intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                startActivity(intent);
            }
        });

        Button runBtn = (Button)findViewById(R.id.case_run_btn);
        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseActivity.this, FloatService.class);
                intent.putExtra(Constant.INTENT_CASE_ID, caseId);
                startService(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        initView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.man_case, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_save_case){
            Toast.makeText(this, "save not ok", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
