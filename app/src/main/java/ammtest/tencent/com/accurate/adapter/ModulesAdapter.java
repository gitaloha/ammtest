package ammtest.tencent.com.accurate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ammtest.tencent.com.accurate.R;
import ammtest.tencent.com.accurate.model.CaseEntryItem;
import ammtest.tencent.com.accurate.util.StringUtil;

/**
 * Created by xiazeng on 2015/11/26.
 */
public class ModulesAdapter extends ArrayAdapter<ModulesItem> {
    int _resource;

    public ModulesAdapter(Context context, int resource, List<ModulesItem> objects) {
        super(context, resource, objects);
        _resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout moduleLy = null;
        if(convertView == null){
            moduleLy = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(_resource, moduleLy, true);
        }else{
            moduleLy = (LinearLayout)convertView;
        }
        ModulesItem item = getItem(position);

        TextView nameTv = (TextView)moduleLy.findViewById(R.id.module_item_name);
        nameTv.setText(item.getModulesName());
        TextView countTv = (TextView)moduleLy.findViewById(R.id.module_item_count);
        countTv.setText(String.valueOf(item.getCount()));
        return moduleLy;
    }


}
