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
    private static final int DB_VERSION = 5;
    private static final String DB_TABLE = "case_tbl";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CaseEntryItem.AUTHORITY, "caseids", CaseEntryItem.CASE_ALL);
        uriMatcher.addURI(CaseEntryItem.AUTHORITY, "caseids/#", CaseEntryItem.CASE_ID);
        uriMatcher.addURI(CaseEntryItem.AUTHORITY, "casename/#", CaseEntryItem.CASE_NAME);
    }

    private static final HashMap<String, String> caseProjectionMap;
    static {
        caseProjectionMap = new HashMap<String, String>();
        caseProjectionMap.put(CaseEntryItem.F_ID, CaseEntryItem.F_ID);
        caseProjectionMap.put(CaseEntryItem.F_CASE_NAME, CaseEntryItem.F_CASE_NAME);
        caseProjectionMap.put(CaseEntryItem.F_CASE_INPUT, CaseEntryItem.F_CASE_INPUT);
        caseProjectionMap.put(CaseEntryItem.F_CASE_OUTPUT, CaseEntryItem.F_CASE_OUTPUT);
        caseProjectionMap.put(CaseEntryItem.F_CASE_CHECK_LIST, CaseEntryItem.F_CASE_CHECK_LIST);
    }

    private AmmtestSQLiteOpenHelper dbHelper = null;
    private ContentResolver resolver = null;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        resolver = context.getContentResolver();
        dbHelper = new AmmtestSQLiteOpenHelper(context, DB_NAME, null, DB_VERSION);

        Log.i(TAG, "Articles Provider Create");

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
            case CaseEntryItem.CASE_ALL: {
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(caseProjectionMap);
                break;
            }
            case CaseEntryItem.CASE_ID: {
                String id = uri.getPathSegments().get(1);
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(caseProjectionMap);
                sqlBuilder.appendWhere(CaseEntryItem.F_ID + "=" + id);
                break;
            }
            case CaseEntryItem.CASE_NAME: {
                String casename = uri.getPathSegments().get(1);
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(caseProjectionMap);
                sqlBuilder.appendWhere(CaseEntryItem.F_CASE_NAME + "=" + casename);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, TextUtils.isEmpty(sortOrder) ? CaseEntryItem.DEFAULT_SORT_ORDER : sortOrder, limit);
        cursor.setNotificationUri(resolver, uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)){
            case CaseEntryItem.CASE_ALL:
            case CaseEntryItem.CASE_ID:
            case CaseEntryItem.CASE_NAME:
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
        if(uriMatcher.match(uri) != CaseEntryItem.CASE_ID) {
            throw new IllegalArgumentException("Error Uri: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id = db.insert(DB_TABLE, CaseEntryItem.F_ID, values);
        if(id < 0) {
            throw new SQLiteException("Unable to insert " + values + " for " + uri);
        }

        Uri newUri = ContentUris.withAppendedId(uri, id);
        resolver.notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;

        switch(uriMatcher.match(uri)) {
            case CaseEntryItem.CASE_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.delete(DB_TABLE, CaseEntryItem.F_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
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
            case CaseEntryItem.CASE_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.update(DB_TABLE, values, CaseEntryItem.F_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        resolver.notifyChange(uri, null);
        return count;
    }
}
