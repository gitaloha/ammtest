package ammtest.tencent.com.ammtest.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by eureka on 11/11/15.
 */
public  class AmmtestSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "";
    private static final String DATABASE_TABLE = "";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "ammtest.AmmtestSQLiteOpenHelper";
    private static final String DATABASE_CREATE_SQL = "create table "+
            DATABASE_TABLE+" (";

    public AmmtestSQLiteOpenHelper(Context context, String name,
                                   SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_SQL);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        Log.i(TAG, "db upgrade,old and new:%d"+i+i2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);

    }

}
