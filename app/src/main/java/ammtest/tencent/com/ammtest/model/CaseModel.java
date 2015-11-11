package ammtest.tencent.com.ammtest.model;

import java.util.ArrayList;
import java.util.List;

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
            caseItems.add(new CaseEntryItem("test"+i));
        }
        return caseItems;
    }

}
