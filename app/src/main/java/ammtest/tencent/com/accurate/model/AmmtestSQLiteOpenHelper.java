package ammtest.tencent.com.accurate.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by eureka on 11/11/15.
 */
public  class AmmtestSQLiteOpenHelper extends SQLiteOpenHelper {
    public static  String DATABASE_NAME = "";
    public static  String DATABASE_CASE_TABLE = "case_tbl";
    public static String DATABASE_EXCUTOR_TABLE = "excutor_tbl";
    private static final String TAG = "ammtest.SqlHelper";
    private static  String DATABASE_CASE_SQL = "create table "+
            DATABASE_CASE_TABLE +" (" +
            CaseEntryItem.F_ID+" integer, "+
            CaseEntryItem.F_CASE_NAME +" text,"+
            CaseEntryItem.F_CASE_MODULE +" text,"+
            CaseEntryItem.F_CASE_INPUT +" text,"+
            CaseEntryItem.F_CASE_OUTPUT +" text,"+
            CaseEntryItem.F_CASE_CHECK_LIST +" text"+
            ")";
    private static String DATABASE_EXCUTOR_SQL = "create table "+
            DATABASE_EXCUTOR_TABLE +"("+
            CaseExcutorItem.F_CASE_ID + " integer, "+
            CaseExcutorItem.F_EXCUTE_USER + " text, "+
            CaseExcutorItem.F_EXCUTE_STATUS + " text," +
            CaseExcutorItem.F_EXCUTE_REVISION + " text," +
            CaseExcutorItem.F_EXCUTE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP"+
            ")";


    public AmmtestSQLiteOpenHelper(Context context, String name,
                                   SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.i(TAG, "AmmtestSQLiteOpenHelper:"+version);
        DATABASE_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, DATABASE_CASE_SQL);
        sqLiteDatabase.execSQL(DATABASE_CASE_SQL);
        sqLiteDatabase.execSQL(DATABASE_EXCUTOR_SQL);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.i(TAG, "db upgrade,old and new:%d"+i+i2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_CASE_TABLE);
        sqLiteDatabase.execSQL(DATABASE_CASE_SQL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_EXCUTOR_TABLE);
        sqLiteDatabase.execSQL(DATABASE_EXCUTOR_SQL);
    }

}
