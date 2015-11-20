package ammtest.tencent.com.accurate.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xiazeng on 2015/11/19.
 */
public class CaseExecutorModel {
    private static final String TAG = "ammtest.CaseExecutorModel";
    private Context mContext;
    ContentResolver resolver;
    String[] projection = new String[] {
            CaseExcutorItem.F_CASE_ID,
            CaseExcutorItem.F_EXCUTE_REVISION,
            CaseExcutorItem.F_EXCUTE_USER,
            CaseExcutorItem.F_EXCUTE_STATUS,
    };

    public CaseExecutorModel(Context context) {
        mContext = context;
        resolver = context.getContentResolver();
    }

    public Uri insert(CaseExcutorItem excutorItem){
        ContentValues values = new ContentValues();
        values.put(CaseExcutorItem.F_CASE_ID, excutorItem.getCaseId());
        values.put(CaseExcutorItem.F_EXCUTE_REVISION, excutorItem.getRevision());
        values.put(CaseExcutorItem.F_EXCUTE_USER, excutorItem.getExcuteUser());
        values.put(CaseExcutorItem.F_EXCUTE_STATUS, excutorItem.getExcuteStatus());
        Uri revisionUri = Uri.parse(CaseProvider.CONTENT_EXCUTOR_URI +"/"+excutorItem.getRevision());
        Uri uri = ContentUris.withAppendedId(revisionUri, excutorItem.getCaseId());
        return resolver.insert(uri, values);
    }

    public boolean update(CaseExcutorItem excutorItem){
        ContentValues values = new ContentValues();
        values.put(CaseExcutorItem.F_CASE_ID, excutorItem.getCaseId());
        values.put(CaseExcutorItem.F_EXCUTE_REVISION, excutorItem.getRevision());
        values.put(CaseExcutorItem.F_EXCUTE_USER, excutorItem.getExcuteUser());
        values.put(CaseExcutorItem.F_EXCUTE_STATUS, excutorItem.getExcuteStatus());
        Uri revisionUri = Uri.parse(CaseProvider.CONTENT_EXCUTOR_URI +"/"+excutorItem.getRevision());
        Uri uri = ContentUris.withAppendedId(revisionUri, excutorItem.getCaseId());
        int count = resolver.update(uri, values, null, null);
        return count > 0;
    }

    private CaseExcutorItem mapToExcutorEntry(Cursor cursor){
        CaseExcutorItem excutorItem = new CaseExcutorItem();
        excutorItem.setCaseId(cursor.getInt(0));
        excutorItem.setRevision(cursor.getString(1));
        excutorItem.setExcuteUser(cursor.getString(2));
        excutorItem.setExcuteStatus(cursor.getInt(3));
        return  excutorItem;
    }

    public CaseExcutorItem getExcutorItem(int caseId, String revision){
        Uri revisionUri = Uri.parse(CaseProvider.CONTENT_EXCUTOR_URI + "/" + revision);
        Uri uri = ContentUris.withAppendedId(revisionUri, caseId);
        Cursor cursor = resolver.query(uri, projection, null, null, null);
        Log.i(TAG, "getExcutorItem:" + caseId + " , " + revision);
        if (null ==cursor || !cursor.moveToFirst()) {
            return null;
        }
        CaseExcutorItem excutorItem = mapToExcutorEntry(cursor);
        return excutorItem;
    }

    public List<CaseExcutorItem> getExcutorsItemsByRevision(String revision){
        List<CaseExcutorItem> caseExcutorsItems = new LinkedList<CaseExcutorItem>();
        Uri revisionUri = Uri.parse(CaseProvider.CONTENT_EXCUTORS_URI +"/"+revision);
        Cursor cursor = resolver.query(revisionUri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                CaseExcutorItem caseExcutorItem = mapToExcutorEntry(cursor);
                caseExcutorsItems.add(caseExcutorItem);
            } while(cursor.moveToNext());
        }

        return caseExcutorsItems;
    }
}
