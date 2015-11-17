package ammtest.tencent.com.accurate.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by eureka on 11/11/15.
 */
public  class AmmtestSQLiteOpenHelper extends SQLiteOpenHelper {
    private static  String DATABASE_NAME = "";
    private static  String DATABASE_TABLE = "case_tbl";
    private static  int DATABASE_VERSION = 5;
    private static final String TAG = "ammtest.SqlHelper";
    private static  String DATABASE_CREATE_SQL = "create table "+
            DATABASE_TABLE+" (" +
            CaseEntryItem.F_ID+" integer, "+
            CaseEntryItem.F_CASE_NAME +" text,"+
            CaseEntryItem.F_CASE_INPUT +" text,"+
            CaseEntryItem.F_CASE_OUTPUT +" text,"+
            CaseEntryItem.F_CASE_CHECK_LIST +" text"+
            ")";

    public AmmtestSQLiteOpenHelper(Context context, String name,
                                   SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.i(TAG, "AmmtestSQLiteOpenHelper:"+version);
        DATABASE_NAME = name;
        DATABASE_VERSION = version;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, DATABASE_CREATE_SQL);
        sqLiteDatabase.execSQL(DATABASE_CREATE_SQL);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.i(TAG, "db upgrade,old and new:%d"+i+i2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        sqLiteDatabase.execSQL(DATABASE_CREATE_SQL);
    }

}
