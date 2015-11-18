package ammtest.tencent.com.accurate;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import ammtest.tencent.com.accurate.model.CaseChosenList;
import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseModel;
import ammtest.tencent.com.accurate.network.AmmHttpClient;


public class MainActivity extends BaseActivity {

    private MainArrayAdapter caseEntryAA;
    private List<CaseEntryItem> caseItems;
    private String TAG = "ammtest.MainActivity";
    private CaseModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mModel = new CaseModel(getApplicationContext());
        caseItems = mModel.getAllCaseItems();
        CaseChosenList.getInstance().bindData(caseItems);
        caseEntryAA = new MainArrayAdapter(this, R.layout.case_item, caseItems);
        ListView lsView = (ListView)findViewById(R.id.main_case_ls);
        lsView.setAdapter(caseEntryAA);

        lsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onclick" + position);
                Intent intent = new Intent(MainActivity.this, CaseDetailFloatService.class);
                intent.putExtra(Constant.INTENT_CASE_ID, caseItems.get(position).getCaseId());
                startService(intent);
                finish();
                //Toast.makeText(MainActivity.this, caseItems.get(position).getCaseName(), Toast.LENGTH_SHORT).show();
            }
        });
        boolean refresh = getIntent().getBooleanExtra(Constant.INTENT_CASE_REFRESH, true);
        if (refresh){
            updateCase();
        }
    }




    private void updateCase(){
        AmmHttpClient.get("getcases.php", null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, response.length() + "");
                Log.i(TAG, response.toString());
                for(int i=0; i<response.length(); i++){
                    try {
                        JSONObject json = response.getJSONObject(i);
                        CaseEntryItem caseEntry = new CaseEntryItem(json.getInt(CaseEntryItem.F_ID));

                        caseEntry.setCaseName(json.getString(CaseEntryItem.F_CASE_NAME));
                        caseEntry.setCaseInput(json.getString(CaseEntryItem.F_CASE_INPUT));
                        caseEntry.setCaseOutput(json.getString(CaseEntryItem.F_CASE_OUTPUT));
                        caseEntry.setCaseCheckList(json.getString(CaseEntryItem.F_CASE_CHECK_LIST));
                        boolean hasContained = false;
                        for(CaseEntryItem item: caseItems){
                            if(caseEntry.getCaseId() == item.getCaseId()){
                                item.updateFrom(caseEntry);
                                hasContained = true;
                                mModel.updateCase(caseEntry);
                                break;
                            }
                        }
                        if(!hasContained){
                            caseItems.add(caseEntry);
                            mModel.insertCase(caseEntry);
                        }
                        caseEntryAA.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, false);

    }
}
