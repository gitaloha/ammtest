package ammtest.tencent.com.accurate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseModel;
import ammtest.tencent.com.accurate.network.AmmHttpClient;


public class MainActivity extends Activity {

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
        caseEntryAA = new MainArrayAdapter(this, R.layout.case_item, caseItems);
        ListView lsView = (ListView)findViewById(R.id.main_case_ls);
        lsView.setAdapter(caseEntryAA);

        lsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onclick" + position);
                //Intent intent = new Intent(MainActivity.this, CaseActivity.class);
                //intent.putExtra(Constant.INTENT_CASE_ID, caseItems.get(position).getCaseId());
                //startActivity(intent);
                Intent intent = new Intent(MainActivity.this, CaseDetailFloatService.class);
                intent.putExtra(Constant.INTENT_CASE_ID, caseItems.get(position).getCaseId());
                startService(intent);
                finish();
                //Toast.makeText(MainActivity.this, caseItems.get(position).getCaseName(), Toast.LENGTH_SHORT).show();
            }
        });
        updateCase();
        /*
        Button btnAdd = (Button)findViewById(R.id.id_case_add_test);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaseEntryItem item = new CaseEntryItem("test"+String.valueOf(new Random().nextInt(1000)));
                item.setHasAuto(new Random().nextBoolean());
                caseItems.add(0, item);
                caseEntryAA.notifyDataSetChanged();
            }
        });
        */

        /*
        Button btnStart = (Button)findViewById(R.id.id_start);
        Button btnStop = (Button)findViewById(R.id.id_stop);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FloatService.class);
                startService(intent);
                finish();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FloatService.class);
                stopService(intent);
            }
        });
        */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.man, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "settingclick", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_new_case){
            Intent intent = new Intent(MainActivity.this, CaseActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateCase(){
        AmmHttpClient.get("getjson.php", null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for(int i=0; i<response.length(); i++){
                    try {
                        JSONObject json = response.getJSONObject(i);
                        CaseEntryItem caseEntry = new CaseEntryItem(json.getInt(CaseEntryItem.F_ID));
                        caseEntry.setCaseName(json.getString(CaseEntryItem.F_CASE_NAME));
                        caseEntry.setCaseInput(json.getString(CaseEntryItem.F_CASE_INPUT));
                        caseEntry.setCaseOutput(json.getString(CaseEntryItem.F_CASE_OUTPUT));
                        caseEntry.setCaseCheckList(json.getString(CaseEntryItem.F_CASE_CHECK_LIST));
                        caseItems.add(caseEntry);
                        mModel.insertCase(caseEntry);
                        caseEntryAA.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i(TAG, response.length() + "");
                Log.i(TAG, response.toString());
            }
        });

    }
}
