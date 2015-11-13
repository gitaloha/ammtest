package ammtest.tencent.com.accurate.model;

import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import java.util.Random;

/**
 * Created by eureka on 10/27/15.
 */
public class CaseEntryItem {
    private static final String TAG = "ammtest.CaseEntryItem";



    String caseModule;
    String caseName;
    String caseInput;
    String caseOutput;
    String caseCheckList;
    int caseId;

    /*Match Code*/
    public static final int CASE_ALL = 1;
    public static final int CASE_ID = 2;
    public static final int CASE_NAME = 3;

    //db table field
    public  static final String F_ID = "case_id";
    public  static final String F_CASE_NAME= "case_name";
    public static final String F_CASE_INPUT = "case_input";
    public static final String F_CASE_OUTPUT = "case_output";
    public static final String F_CASE_CHECK_LIST = "case_check_list";

    /*Default sort order*/
    public static final String DEFAULT_SORT_ORDER = F_ID+" asc";

    /*Authority*/
    public static final String AUTHORITY = "com.tencent.ammtest.mm.cases";

    /*Content URI*/
    public static final Uri CONTENT_CASEID_URI = Uri.parse("content://" + AUTHORITY + "/caseids");
    public static final Uri CONTENT_CASENAME_URI = Uri.parse("content://" + AUTHORITY + "/casename");

    /*MIME*/
    public static final String CONTENT_TYPE_CASE = "vnd.android.cursor.dir/vnd.shy.luo.article";

    public String getCaseName() {
        return caseName;
    }

    public void setCaseModule(String caseModule) {
        this.caseModule = caseModule;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }


    public String getCaseModule() {
        return caseModule;
    }


    public void setCaseInput(String caseInput) {
        this.caseInput = caseInput;
    }

    public void setCaseOutput(String caseOutput) {
        this.caseOutput = caseOutput;
    }

    public void setCaseCheckList(String caseCheckList) {
        this.caseCheckList = caseCheckList;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

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

    public CaseEntryItem(int caseId) {
        this.caseId = caseId;
    }

    public CaseEntryItem(String name) {
        this.caseName = name;
        Log.i(TAG, "CaseEntryItem:"+name);
        caseModule= "caseModel caseModuel";
        caseInput = "caseInput caseInput";
        caseOutput = "caseOutput caseOutput";
        hasAuto = false;
        count = 0;
        caseId = new Random().nextInt(10000);
    }


    @Override
    public String toString() {
        return caseName;
    }

    public String getCaseInput() {
        return caseInput;
    }

    public String getCaseOutput() {
        return caseOutput;
    }

    public String getCaseCheckList() {
        return caseCheckList;
    }
}
