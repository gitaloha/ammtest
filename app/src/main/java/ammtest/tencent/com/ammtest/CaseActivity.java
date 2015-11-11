package ammtest.tencent.com.ammtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import ammtest.tencent.com.ammtest.model.CaseEntryItem;
import ammtest.tencent.com.ammtest.model.CaseModel;


public class CaseActivity extends Activity {
    int caseId;
    String caseName;
    private String TAG = "ammtet.CaseActivity";

    void initView(){
        Intent intent = getIntent();
        caseId = intent.getIntExtra("caseId", 0);
        CaseEntryItem entryItem = CaseModel.getInstance().getCaseItem(caseId);
        TextView titleTv = (TextView)findViewById(R.id.case_title);
        titleTv.setText(String.format("【%d】%s", caseId, entryItem.getCaseName()));
        TextView contentTx = (TextView)findViewById(R.id.case_content);
        contentTx.setText(entryItem.getCaseContent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);
        initView();
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
