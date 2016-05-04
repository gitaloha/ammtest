package ammtest.tencent.com.accurate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import ammtest.tencent.com.accurate.R;
import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.model.CaseExcutorItem;
import ammtest.tencent.com.accurate.model.CaseExecutorModel;
import ammtest.tencent.com.accurate.network.AccurateClient;
import ammtest.tencent.com.accurate.util.StringUtil;

/**
 * Created by eureka on 10/27/15.
 */
public class MainArrayAdapter extends ArrayAdapter<CaseEntryItem> {
    int _resource;
    Context context;
    CaseExecutorModel caseModule;

    public MainArrayAdapter(Context context, int resource, List<CaseEntryItem> objects) {
        super(context, resource, objects);
        this.context = context;
        caseModule = new CaseExecutorModel(context);
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

        CaseEntryItem item = getItem(position);


        TextView caseNameView = (TextView)caseLy.findViewById(R.id.case_item_name);
        caseNameView.setText(item.getCaseName());

        TextView caseIdView = (TextView)caseLy.findViewById(R.id.case_item_id);
        caseIdView.setText(StringUtil.caseIdToStr(item.getCaseId()));
        List<CaseExcutorItem> excutorItems = caseModule.getExcutorItem(item.getCaseId());
        TextView countTv = (TextView)caseLy.findViewById(R.id.case_item_num);
        if (null == excutorItems){
            countTv.setText(String.valueOf(new Random().nextInt(1)));
        }else {
            countTv.setText(String.valueOf(excutorItems.size()));
        }
        //inputTv.setText(item.getCaseInput());
        return (LinearLayout)caseLy;

    }
}
