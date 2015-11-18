package ammtest.tencent.com.accurate.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiazeng on 2015/11/18.
 */
public class CaseChosenList {


    static  private CaseChosenList instance = null;
    private List<CaseEntryItem> casesEntries;

    static public CaseChosenList getInstance(){
        if(instance == null){
            instance = new CaseChosenList();
        }
        return instance;
    }
    public CaseChosenList() {
        casesEntries = new ArrayList<CaseEntryItem>();
    }
    public void bindData(List<CaseEntryItem> datas){
        casesEntries = datas;
    }

    public int getCount(){
        return casesEntries.size();
    }
    public int getIndex(int caseId){
        int index = 0;
        for (CaseEntryItem caseEntryItem: casesEntries){
            index +=1;
            if(caseEntryItem.getCaseId() == caseId){
                break;
            }
        }
        return index;
    }

    public CaseEntryItem getNextCase(int caseId){
        boolean isFind = false;
        for(CaseEntryItem caseEntryItem: casesEntries){
            if(isFind){
                return caseEntryItem;
            }
            if(caseEntryItem.getCaseId() == caseId){
                isFind = true;
            }
        }
        return null;
    }
}
