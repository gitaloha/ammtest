package ammtest.tencent.com.ammtest.model;

import java.util.Random;

/**
 * Created by eureka on 10/27/15.
 */
public class CaseEntryItem {
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
    long caseId;

    public boolean isHasAuto() {
        return hasAuto;
    }

    public void setHasAuto(boolean hasAuto) {
        this.hasAuto = hasAuto;
    }

    boolean hasAuto;
    int count;

    public long getCaseId() {
        return caseId;
    }

    public CaseEntryItem(String name) {
        this.caseName = name;
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
