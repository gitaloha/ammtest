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

import java.util.List;

import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseModel;


public class MainActivity extends Activity {

    private MainArrayAdapter caseEntryAA;
    private List<CaseEntryItem> caseItems;
    private String TAG = "ammtest.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        caseItems = CaseModel.getInstance().getAllCaseItems();
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
}
