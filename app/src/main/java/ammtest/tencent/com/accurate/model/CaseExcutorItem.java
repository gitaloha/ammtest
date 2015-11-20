package ammtest.tencent.com.accurate.model;

import java.util.Date;

import ammtest.tencent.com.accurate.network.AccurateClient;

/**
 * Created by xiazeng on 2015/11/19.
 */
public class CaseExcutorItem {

    String revision;
    int caseId;
    Date excuteTime;
    int excuteStatus;
    String excuteUser;
    public static final int EXCUTE_STATUS_FAILED = 0;
    public static final int EXCUTE_STATUS_SUCCESS = 1;
    public static final int EXCUTE_STATUS_IGNORE = 2;

    public  static final String F_CASE_ID = "case_id";
    public  static final String F_EXCUTE_TIME= "excuteTime";
    public static final String F_EXCUTE_STATUS = "excuteStatus";
    public static final String F_EXCUTE_USER = "excuteUser";
    public static final String F_EXCUTE_REVISION = "excuteRevision";

    public CaseExcutorItem() {
        revision = AccurateClient.revision;
        excuteStatus = EXCUTE_STATUS_IGNORE;
        excuteUser = User.getInstance().getName();
    }

    public void setPass(){
        excuteStatus = EXCUTE_STATUS_SUCCESS;
    }
    public void setFailure(){
        excuteStatus = EXCUTE_STATUS_FAILED;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getExcuteStatus() {
        return excuteStatus;
    }

    public void setExcuteStatus(int excuteStatus) {
        this.excuteStatus = excuteStatus;
    }

    public String getExcuteUser() {
        return excuteUser;
    }

    public void setExcuteUser(String excuteUser) {
        this.excuteUser = excuteUser;
    }

    @Override
    public String toString() {
        return "CaseExcutorItem{" +
                "revision='" + revision + '\'' +
                ", caseId=" + caseId +
                ", excuteTime=" + excuteTime +
                ", excuteStatus=" + excuteStatus +
                ", excuteUser='" + excuteUser + '\'' +
                '}';
    }
}
