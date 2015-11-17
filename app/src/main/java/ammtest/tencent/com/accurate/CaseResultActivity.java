package ammtest.tencent.com.accurate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ammtest.tencent.com.accurate.model.CaseEntryItem;

public class CaseResultActivity extends Activity {
    private  int caseId;
    private String TAG = "ammtest.CaseResultActivity";
    private CaseEntryItem caseEntryItem;
    private Map<Long, ArrayList<MethodItem>> caseResult = new HashMap<Long, ArrayList<MethodItem>>();
    static final int HANDLER_STATISTIC = 1;
    static final int HANDLER_DETAIL = 2;
    private Handler handler = new Handler();
    private ArrayList<CaseResultItem> caseResults;
    private CaseResultAdapter adapter;


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
        caseId = intent.getIntExtra(Constant.INTENT_CASE_ID, 0);
        final String caseFilename = intent.getStringExtra(Constant.INTENT_CASE_FILENAME);
        caseResults = new ArrayList<CaseResultItem>();
        adapter = new CaseResultAdapter(CaseResultActivity.this, R.layout.case_result, caseResults);
        ListView lsView = (ListView)findViewById(R.id.case_result_list);
        lsView.setAdapter(adapter);

        new Thread(){
            int methodCount = 0;
            int threadCount = 0;
            @Override
            public void run() {
                final Set<Long> methodSet = new HashSet<Long>();
                final File file = new File(caseFilename);
                Log.i(TAG, "case result file path:"+file.getPath());
                if(!file.exists()){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CaseResultActivity.this, "not found:"+file.getPath(), Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }
                try {
                    String line = null;
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    while ((line = reader.readLine()) != null) {
                        String [] ls = line.split(",");
                        if(ls.length != 3){
                            Log.w(TAG, "unvalided line:"+line);
                            continue;
                        }
                        long methodId = Integer.parseInt(ls[0]);
                        String threadName = ls[2];
                        long threadId = Integer.parseInt(ls[1]);
                        MethodItem methodItem = new MethodItem(methodId, threadName, threadId);
                        methodSet.add(methodId);
                        if(caseResult.keySet().contains(threadId)){
                            caseResult.get(threadId).add(methodItem);
                        }else{
                            threadCount+=1;
                            ArrayList<MethodItem> methods = new ArrayList<MethodItem>();
                            methods.add(methodItem);
                            caseResult.put(threadId, methods);
                        }
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Map.Entry<Long, ArrayList<MethodItem>> entry : caseResult.entrySet()) {
                    CaseResultItem caseEntry = new CaseResultItem(entry.getValue());
                    caseResults.add(caseEntry);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView threadTv = (TextView) findViewById(R.id.check_thread_statistic);
                        threadTv.setText(String.valueOf(threadCount));
                        TextView methodTv = (TextView) findViewById(R.id.check_method_statistic);
                        methodTv.setText(String.valueOf(methodSet.size()));
                        adapter.notifyDataSetChanged();
                    }
                });



            }
        }.start();

    }

    static public class CaseResultItem{
        String threadName;
        int methodCount;
        int methodDistinctCount;

        public CaseResultItem(ArrayList<MethodItem> methods) {
            if (methods.size() > 0) {
                threadName = methods.get(0).threadName;
                methodCount = methods.size();
                Set<MethodItem> methodSet = new HashSet<>();
                methodSet.addAll(methods);
                methodDistinctCount = methodSet.size();
            }
        }
    }
    static public class CaseResultAdapter extends ArrayAdapter<CaseResultItem> {
        int _resource;

        public CaseResultAdapter(Context context, int resource, List<CaseResultItem> objects) {
            super(context, resource, objects);
            _resource = resource;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout caseLy;
            if(convertView == null){
                caseLy = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater li = (LayoutInflater)getContext().getSystemService(inflater);
                li.inflate(_resource, caseLy, true);
            }else{
                caseLy = (LinearLayout)convertView;
            }

            CaseResultItem item = getItem(position);

            TextView threadName = (TextView)caseLy.findViewById(R.id.case_result_thread_name);
            threadName.setText(item.threadName);

            TextView methodCount = (TextView)caseLy.findViewById(R.id.case_result_method_count);
            methodCount.setText(String.valueOf(item.methodCount));

            TextView methodDistinctCount = (TextView)caseLy.findViewById(R.id.case_result_method_distinct_count);
            methodDistinctCount.setText(String.valueOf(item.methodDistinctCount));
            return (LinearLayout)caseLy;
        }


    }
}
