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



    //db table field
    public  static final String F_ID = "case_id";
    public  static final String F_CASE_NAME= "case_name";
    public static final String F_CASE_INPUT = "case_input";
    public static final String F_CASE_OUTPUT = "case_output";
    public static final String F_CASE_CHECK_LIST = "case_check_list";
    public static final String F_CASE_MODULE = "case_module";






    /*MIME*/
    public static final String CONTENT_TYPE_CASE = "vnd.android.cursor.dir/vnd.shy.luo.article";

    public void updateFrom(CaseEntryItem other){
        this.caseId = other.caseId;
        this.caseInput = other.caseInput;
        this.caseOutput = other.caseOutput;
        this.caseCheckList = other.caseCheckList;
        this.caseModule = other.caseModule;
    }

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




    public String getCaseInput() {
        return caseInput;
    }

    public String getCaseOutput() {
        return caseOutput;
    }

    public String getCaseCheckList() {
        return caseCheckList;
    }

    @Override
    public String toString() {
        return "CaseEntryItem{" +
                "caseModule='" + caseModule + '\'' +
                ", caseName='" + caseName + '\'' +
                ", caseInput='" + caseInput + '\'' +
                ", caseOutput='" + caseOutput + '\'' +
                ", caseCheckList='" + caseCheckList + '\'' +
                ", caseId=" + caseId +
                ", hasAuto=" + hasAuto +
                ", count=" + count +
                '}';
    }
}
