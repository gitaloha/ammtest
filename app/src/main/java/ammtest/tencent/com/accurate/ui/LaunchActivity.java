package ammtest.tencent.com.accurate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ammtest.tencent.com.accurate.R;
import ammtest.tencent.com.accurate.adapter.ModulesAdapter;
import ammtest.tencent.com.accurate.adapter.ModulesItem;
import ammtest.tencent.com.accurate.model.CaseExecutorModel;
import ammtest.tencent.com.accurate.model.CaseModel;

public class LaunchActivity extends BaseActivity {

    private CaseModel mModel;
    private CaseExecutorModel mExcutorModel;
    private ArrayList<ModulesItem> modulesItems;
    private ModulesAdapter moduleEntryAA;

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

}
