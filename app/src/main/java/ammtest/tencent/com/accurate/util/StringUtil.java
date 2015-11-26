package ammtest.tencent.com.accurate.util;

/**
 * Created by xiazeng on 2015/11/26.
 */
public class StringUtil {
    static public  String caseIdToStr(int caseId){
        String str = String.format("%06d", caseId);
        return str;
    }


}
