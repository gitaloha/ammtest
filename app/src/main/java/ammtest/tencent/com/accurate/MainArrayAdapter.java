package ammtest.tencent.com.accurate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ammtest.tencent.com.accurate.model.CaseEntryItem;

/**
 * Created by eureka on 10/27/15.
 */
public class MainArrayAdapter extends ArrayAdapter<CaseEntryItem> {
    int _resource;

    public MainArrayAdapter(Context context, int resource, List<CaseEntryItem> objects) {
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

        CaseEntryItem item = getItem(position);

        TextView caseNameView = (TextView)caseLy.findViewById(R.id.case_item_name);
        caseNameView.setText(item.getCaseName());

        TextView hasAutoView = (TextView)caseLy.findViewById(R.id.case_has_auto);
        hasAutoView.setText(String.valueOf(item.isHasAuto()));



        return (LinearLayout)caseLy;

    }
}
