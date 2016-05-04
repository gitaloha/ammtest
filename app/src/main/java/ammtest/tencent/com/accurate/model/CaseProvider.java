package ammtest.tencent.com.accurate.model;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by xiazeng on 2015/11/13.
 */
public class CaseProvider extends ContentProvider {
    static final String TAG = "ammtest.CaseProvider";

    private static final UriMatcher uriMatcher;
    private static final String DB_NAME = "case";
    private static final int DB_VERSION = 10;
    private static final String DB_CASE_TABLE = AmmtestSQLiteOpenHelper.DATABASE_CASE_TABLE;
    private static final String DB_EXCUTOR_TABLE = AmmtestSQLiteOpenHelper.DATABASE_EXCUTOR_TABLE;

    /*Authority*/
    public static final String AUTHORITY = "com.tencent.ammtest.mm.cases";

    /*Content URI*/
    public static final Uri CONTENT_CASEID_URI = Uri.parse("content://" + AUTHORITY + "/caseids");
    public static final Uri CONTENT_EXCUTOR_URI = Uri.parse("content://" + AUTHORITY + "/excutor");
    public static final Uri CONTENT_EXCUTORS_URI = Uri.parse("content://" + AUTHORITY + "/excutors");

    /*Match Code*/
    public static final int CASE_ALL = 1;
    public static final int CASE_ID = 2;
    public static final int CASE_MODULES = 3;
    public static final int CASE_EXCUTORS = 4;
    public static final int CASE_EXCUTOR = 5;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "caseids", CASE_ALL);
        uriMatcher.addURI(AUTHORITY, "caseids/#", CASE_ID);
        uriMatcher.addURI(AUTHORITY, "casemodules/#", CASE_MODULES);
        uriMatcher.addURI(AUTHORITY, "excutors/*", CASE_EXCUTORS);
        uriMatcher.addURI(AUTHORITY, "excutor/*/#", CASE_EXCUTOR);
    }

    private static final HashMap<String, String> caseEntryProjectionMap;
    static {
        caseEntryProjectionMap = new HashMap<String, String>();
        caseEntryProjectionMap.put(CaseEntryItem.F_ID, CaseEntryItem.F_ID);
        caseEntryProjectionMap.put(CaseEntryItem.F_CASE_NAME, CaseEntryItem.F_CASE_NAME);
        caseEntryProjectionMap.put(CaseEntryItem.F_CASE_INPUT, CaseEntryItem.F_CASE_INPUT);
        caseEntryProjectionMap.put(CaseEntryItem.F_CASE_OUTPUT, CaseEntryItem.F_CASE_OUTPUT);
        caseEntryProjectionMap.put(CaseEntryItem.F_CASE_MODULE, CaseEntryItem.F_CASE_MODULE);
        caseEntryProjectionMap.put(CaseEntryItem.F_CASE_CHECK_LIST, CaseEntryItem.F_CASE_CHECK_LIST);
    }

    private static final HashMap<String, String> caseModuleProjectionMap;
    static {
        caseModuleProjectionMap = new HashMap<String, String>();
        caseModuleProjectionMap.put(CaseEntryItem.F_CASE_MODULE, CaseEntryItem.F_CASE_MODULE);
    }

    private AmmtestSQLiteOpenHelper dbHelper = null;
    private ContentResolver resolver = null;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        resolver = context.getContentResolver();
        dbHelper = new AmmtestSQLiteOpenHelper(context, DB_NAME, null, DB_VERSION);

        Log.i(TAG, " Provider Create");

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.i(TAG, "ArticlesProvider.query: " + uri);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        String limit = null;

        switch (uriMatcher.match(uri)) {
            case CASE_ALL: {
                sqlBuilder.setTables(DB_CASE_TABLE);
                break;
            }
            case CASE_ID: {
                String id = uri.getPathSegments().get(1);
                sqlBuilder.setTables(DB_CASE_TABLE);
                sqlBuilder.appendWhere(CaseEntryItem.F_ID + "=" + id);
                break;
            }
            case CASE_MODULES: {
                String moduleName = uri.getPathSegments().get(1);
                sqlBuilder.setTables(DB_CASE_TABLE);
                sqlBuilder.appendWhere(CaseEntryItem.F_CASE_MODULE + "=" + moduleName);
                break;
            }
            case CASE_EXCUTOR:{
                //String revision = uri.getPathSegments().get(1);
                String idStr = uri.getPathSegments().get(2);
                sqlBuilder.setTables(DB_EXCUTOR_TABLE);
                sqlBuilder.appendWhere(CaseExcutorItem.F_CASE_ID + "=" + idStr);
                //sqlBuilder.appendWhere(" and "+CaseExcutorItem.F_EXCUTE_REVISION+"='"+revision+"'");
                break;
            }
            case CASE_EXCUTORS:{
                //获取某个版本执行的所有CASE
                String revision = uri.getPathSegments().get(1);
                sqlBuilder.setTables(DB_EXCUTOR_TABLE);
                sqlBuilder.appendWhere(CaseExcutorItem.F_EXCUTE_REVISION+"='"+revision+"'");
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, TextUtils.isEmpty(sortOrder) ? null : sortOrder, limit);
        cursor.setNotificationUri(resolver, uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)){
            case CASE_ALL:
            case CASE_ID:
            case CASE_MODULES:
            case CASE_EXCUTOR:
            case CASE_EXCUTORS:
                return  CaseEntryItem.CONTENT_TYPE_CASE;
            default:
                throw  new IllegalArgumentException("Error Uri: " + uri);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newUri = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (uriMatcher.match(uri) == CASE_EXCUTOR){
            long id = db.insert(DB_EXCUTOR_TABLE, CaseExcutorItem.F_CASE_ID, values);
            if(id < 0) {
                throw new SQLiteException("Unable to insert " + values + " for " + uri);
            }
            newUri = ContentUris.withAppendedId(uri, id);
            resolver.notifyChange(newUri, null);
        }
        else if(uriMatcher.match(uri) ==CASE_ID) {
            long id = db.insert(DB_CASE_TABLE, CaseEntryItem.F_ID, values);
            if(id < 0) {
                throw new SQLiteException("Unable to insert " + values + " for " + uri);
            }
            newUri = ContentUris.withAppendedId(uri, id);
            resolver.notifyChange(newUri, null);
        }
        else{
            throw new IllegalArgumentException("Error Uri: " + uri);
        }

        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;

        switch(uriMatcher.match(uri)) {
            case CASE_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.delete(DB_CASE_TABLE, CaseEntryItem.F_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            case CASE_EXCUTOR:{
                String revisions = uri.getPathSegments().get(1);
                String id = uri.getPathSegments().get(2);
                count = db.delete(DB_EXCUTOR_TABLE, CaseExcutorItem.F_CASE_ID +"="+id+
                " and "+CaseExcutorItem.F_EXCUTE_REVISION+"="+revisions+
                        (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        resolver.notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;

        switch(uriMatcher.match(uri)) {
            case CASE_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.update(DB_CASE_TABLE, values, CaseEntryItem.F_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            case CASE_EXCUTOR:{
                String revisions = uri.getPathSegments().get(1);
                String id = uri.getPathSegments().get(2);
                count = db.update(DB_EXCUTOR_TABLE, values, CaseExcutorItem.F_CASE_ID + "=" + id +
                        " and " + CaseExcutorItem.F_EXCUTE_REVISION + "='" + revisions +"'"+
                        (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        resolver.notifyChange(uri, null);
        return count;
    }
}
