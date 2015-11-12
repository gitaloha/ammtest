package ammtest.tencent.com.accurate.model;

import android.nfc.Tag;
import android.util.Log;

import java.util.Random;

/**
 * Created by eureka on 10/27/15.
 */
public class CaseEntryItem {
    private static final String TAG = "ammtest.CaseEntryItem";

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    String caseName;

    public String getCaseContent() {
        return caseContent;
    }

    String caseContent;
    int caseId;

    public boolean isHasAuto() {
        return hasAuto;
    }

    public void setHasAuto(boolean hasAuto) {
        this.hasAuto = hasAuto;
    }

    boolean hasAuto;
    int count;

    public int getCaseId() {
        return caseId;
    }

    public CaseEntryItem(String name) {
        this.caseName = name;
        Log.i(TAG, "CaseEntryItem:"+name);
        caseContent = "caseContentcaseContentcaseContentcaseContentcaseContentcaseContentcaseContentcaseContentcaseContent";
        hasAuto = false;
        count = 0;
        caseId = new Random().nextInt(10000);
    }


    @Override
    public String toString() {
        return caseName;
    }
}
