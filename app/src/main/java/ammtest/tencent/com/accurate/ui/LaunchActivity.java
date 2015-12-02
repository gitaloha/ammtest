package ammtest.tencent.com.accurate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ammtest.tencent.com.accurate.R;
import ammtest.tencent.com.accurate.adapter.ModulesAdapter;
import ammtest.tencent.com.accurate.adapter.ModulesItem;
import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseExecutorModel;
import ammtest.tencent.com.accurate.model.CaseModel;
import ammtest.tencent.com.accurate.network.AmmHttpClient;

public class LaunchActivity extends BaseActivity {

    private CaseModel mModel;
    private CaseExecutorModel mExcutorModel;
    private ArrayList<ModulesItem> modulesItems;
    private ModulesAdapter moduleEntryAA;
    private String TAG = "ammtest.LaunchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        mModel = new CaseModel(getApplicationContext());
        mExcutorModel = new CaseExecutorModel(getApplicationContext());

        modulesItems = mModel.getCaseModules();
        moduleEntryAA = new ModulesAdapter(this, R.layout.launch_module_item, modulesItems);
        ListView lsView = (ListView)findViewById(R.id.launch_list);
        lsView.setAdapter(moduleEntryAA);
        lsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LaunchActivity.this, ModuleCasesActivity.class);
                intent.putExtra(Constant.INTENT_MODULE_NAME, modulesItems.get(position).getModulesName());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateCase();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateCase(){
        AmmHttpClient.get("getcases.php", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, response.length() + "");
                Log.i(TAG, response.toString());
                List<CaseEntryItem> caseItems = mModel.getAllCaseItems();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject json = response.getJSONObject(i);
                        CaseEntryItem caseEntry = new CaseEntryItem(json.getInt(CaseEntryItem.F_ID));

                        caseEntry.setCaseName(json.getString(CaseEntryItem.F_CASE_NAME));
                        caseEntry.setCaseInput(json.getString(CaseEntryItem.F_CASE_INPUT));
                        caseEntry.setCaseOutput(json.getString(CaseEntryItem.F_CASE_OUTPUT));
                        caseEntry.setCaseModule(json.getString(CaseEntryItem.F_CASE_MODULE));
                        caseEntry.setCaseCheckList(json.getString(CaseEntryItem.F_CASE_CHECK_LIST));
                        boolean hasContained = false;
                        for (CaseEntryItem item : caseItems) {
                            if (caseEntry.getCaseId() == item.getCaseId()) {
                                item.updateFrom(caseEntry);
                                hasContained = true;
                                mModel.updateCase(caseEntry);
                                break;
                            }
                        }
                        if (!hasContained) {
                            caseItems.add(caseEntry);
                            mModel.insertCase(caseEntry);
                        }
                        ArrayList<ModulesItem> models = mModel.getCaseModules();
                        modulesItems.clear();
                        modulesItems.addAll(models);
                        moduleEntryAA.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, false);

    }

}
