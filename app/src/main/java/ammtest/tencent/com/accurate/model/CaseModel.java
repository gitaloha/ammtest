package ammtest.tencent.com.accurate.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ammtest.tencent.com.accurate.R;

/**
 * Created by eureka on 11/12/15.
 */
public class CaseModel {
    public static CaseModel instance = null;


    private CaseModel() {

    }
    public static CaseModel getInstance(){

        if(instance == null){
            instance = new CaseModel();
        }
        return instance;
    }

    public CaseEntryItem getCaseItem(int caseId){
        return new CaseEntryItem("test"+caseId);
    }

    public List<CaseEntryItem> getAllCaseItems(){
        List<CaseEntryItem>  caseItems = new ArrayList<CaseEntryItem>();
        for (int i=0; i<10; i++){
            caseItems.add(new CaseEntryItem("test"+String.valueOf(i)));
        }
        return caseItems;
    }

}
