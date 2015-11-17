package ammtest.tencent.com.accurate.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ammtest.tencent.com.accurate.R;

/**
 * Created by eureka on 11/12/15.
 */
public class CaseModel {
    private static final String TAG = "ammtest.CaseModel";
    public static CaseModel instance = null;
    private Context mContext;
    ContentResolver resolver;
    String[] projection = new String[] {
            CaseEntryItem.F_ID,
            CaseEntryItem.F_CASE_NAME,
            CaseEntryItem.F_CASE_INPUT,
            CaseEntryItem.F_CASE_OUTPUT,
            CaseEntryItem.F_CASE_CHECK_LIST
    };

    public CaseModel(Context context) {
        mContext = context;
         resolver = context.getContentResolver();
    }
    /*
    public static CaseModel getInstance(){

        if(instance == null){
            instance = new CaseModel();
        }
        return instance;
    }*/

    void addCaseEntryItem(CaseEntryItem caseEntryItem){

    }

    public  boolean removeCase(int caseId){
        Uri uri = ContentUris.withAppendedId(CaseEntryItem.CONTENT_CASEID_URI, caseId);
        int count = resolver.delete(uri, null, null);
        return count>0;
    }

    public Uri insertCase(CaseEntryItem caseEntryItem) {
        ContentValues values = new ContentValues();
        values.put(CaseEntryItem.F_ID, caseEntryItem.getCaseId());
        values.put(CaseEntryItem.F_CASE_NAME, caseEntryItem.getCaseName());
        values.put(CaseEntryItem.F_CASE_INPUT, caseEntryItem.getCaseInput());
        values.put(CaseEntryItem.F_CASE_OUTPUT, caseEntryItem.getCaseOutput());
        values.put(CaseEntryItem.F_CASE_CHECK_LIST, caseEntryItem.getCaseCheckList());
        Uri uri = ContentUris.withAppendedId(CaseEntryItem.CONTENT_CASEID_URI, caseEntryItem.getCaseId());

        return resolver.insert(uri, values);
    }

    public boolean updateCase(CaseEntryItem caseEntryItem) {
        Uri uri = ContentUris.withAppendedId(CaseEntryItem.CONTENT_CASEID_URI, caseEntryItem.getCaseId());

        ContentValues values = new ContentValues();
        values.put(CaseEntryItem.F_CASE_NAME, caseEntryItem.getCaseName());
        values.put(CaseEntryItem.F_CASE_INPUT, caseEntryItem.getCaseInput());
        values.put(CaseEntryItem.F_CASE_OUTPUT, caseEntryItem.getCaseOutput());
        values.put(CaseEntryItem.F_CASE_CHECK_LIST, caseEntryItem.getCaseCheckList());

        int count = resolver.update(uri, values, null, null);

        return count > 0;
    }

    public CaseEntryItem getCaseItem(int caseId){
        Uri uri = ContentUris.withAppendedId(CaseEntryItem.CONTENT_CASEID_URI, caseId);
        Cursor cursor = resolver.query(uri, projection, null, null, CaseEntryItem.DEFAULT_SORT_ORDER);
        Log.i(TAG, "getCaseItem:"+caseId);

        if (!cursor.moveToFirst()) {
            return null;
        }
        int id = cursor.getInt(0);
        CaseEntryItem caseEntryItem = new CaseEntryItem(id);
        caseEntryItem.setCaseName(cursor.getString(1));
        caseEntryItem.setCaseInput(cursor.getString(2));
        caseEntryItem.setCaseOutput(cursor.getString(3));
        caseEntryItem.setCaseCheckList(cursor.getString(4));
        return caseEntryItem;
    }

    public List<CaseEntryItem> getAllCaseItems(){
        LinkedList<CaseEntryItem> caseEntryItems = new LinkedList<CaseEntryItem>();
        Cursor cursor = resolver.query(CaseEntryItem.CONTENT_CASEID_URI, projection, null, null, CaseEntryItem.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                CaseEntryItem caseEntryItem = new CaseEntryItem(id);
                caseEntryItem.setCaseName(cursor.getString(1));
                caseEntryItem.setCaseInput(cursor.getString(2));
                caseEntryItem.setCaseOutput(cursor.getString(3));
                caseEntryItem.setCaseCheckList(cursor.getString(4));
                caseEntryItems.add(caseEntryItem);
            } while(cursor.moveToNext());
        }

        return caseEntryItems;
    }

    public List<CaseEntryItem> getCaseItemsByCaseNames(String name){
        LinkedList<CaseEntryItem> caseEntryItems = new LinkedList<CaseEntryItem>();
        Cursor cursor = resolver.query(CaseEntryItem.CONTENT_CASEID_URI, projection,
                CaseEntryItem.F_CASE_NAME+ "='"+name+"'", null, CaseEntryItem.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                CaseEntryItem caseEntryItem = new CaseEntryItem(id);
                caseEntryItem.setCaseName(cursor.getString(1));
                caseEntryItem.setCaseInput(cursor.getString(2));
                caseEntryItem.setCaseOutput(cursor.getString(3));
                caseEntryItem.setCaseCheckList(cursor.getString(4));
                caseEntryItems.add(caseEntryItem);
            } while(cursor.moveToNext());
        }
        return caseEntryItems;
    }

}
