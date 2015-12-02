package ammtest.tencent.com.accurate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ammtest.tencent.com.accurate.R;
import ammtest.tencent.com.accurate.adapter.MainArrayAdapter;
import ammtest.tencent.com.accurate.model.CaseChosenList;
import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseExcutorItem;
import ammtest.tencent.com.accurate.model.CaseExecutorModel;
import ammtest.tencent.com.accurate.model.CaseModel;
import ammtest.tencent.com.accurate.network.AmmHttpClient;


public class ModuleCasesActivity extends BaseActivity {

    private MainArrayAdapter caseEntryAA;
    private List<CaseEntryItem> caseItems;
    private String TAG = "ammtest.ModuleCasesActivity";
    private CaseModel mModel;
    protected CaseExecutorModel mExcutorModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String moduleName = intent.getStringExtra(Constant.INTENT_MODULE_NAME);

        mModel = new CaseModel(getApplicationContext());
        mExcutorModel = new CaseExecutorModel(getApplicationContext());

        //caseItems = mModel.getAllCaseItems();
        caseItems = mModel.getCaseItemsByModuleName(moduleName);
        CaseChosenList.getInstance().bindData(caseItems);
        caseEntryAA = new MainArrayAdapter(this, R.layout.case_item, caseItems);
        ListView lsView = (ListView)findViewById(R.id.main_case_ls);
        lsView.setAdapter(caseEntryAA);
        lsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onclick" + position);
                Intent intent = new Intent(ModuleCasesActivity.this, FloatService.class);
                intent.putExtra(Constant.INTENT_CASE_ID, caseItems.get(position).getCaseId());
                startService(intent);
                finish();
                //Toast.makeText(ModuleCasesActivity.this, caseItems.get(position).getCaseName(), Toast.LENGTH_SHORT).show();
            }
        });
        boolean refresh = getIntent().getBooleanExtra(Constant.INTENT_CASE_REFRESH, false);

        //test
        new Thread(new Runnable(){

            @Override
            public void run() {
                /*
                CaseExcutorItem excutorItem = new CaseExcutorItem();
                excutorItem.setCaseId(0);
                mExcutorModel.insert(excutorItem);
                excutorItem.setCaseId(1);
                mExcutorModel.insert(excutorItem);
                CaseExcutorItem result = mExcutorModel.getExcutorItem(0, "revision");
                Log.i(TAG, result.toString());
                List<CaseExcutorItem> results = mExcutorModel.getExcutorsItemsByRevision("revision");
                Log.i(TAG, results.toString());
                excutorItem.setCaseId(1);
                excutorItem.setExcuteUser("susan");
                mExcutorModel.update(excutorItem);
                */
                List<CaseExcutorItem> results = mExcutorModel.getExcutorsItemsByRevision("norevision");
                Log.i(TAG, results.toString());

                //ArrayList<String> modules = mModel.getCaseModules();
                //Log.i(TAG, modules.toString());
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            back();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void back(){
        Intent intent = new Intent(ModuleCasesActivity.this, LaunchActivity.class);
        startActivity(intent);
        finish();
    }


}
