package cn.xavier.movie.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yangxh on 17/7/30.
 */

public class DbOpenHelper extends SQLiteOpenHelper {
    private static DbOpenHelper instantce;

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public static DbOpenHelper getInstance(Context context){
        if(instantce==null){
            synchronized (DbOpenHelper.class){
                if(instantce==null){
                    instantce=new DbOpenHelper(context.getApplicationContext(),DbConstant.DB_NAME,null,DbConstant.DB_VERSION);
                }
            }
        }
        return instantce;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstant.CREATE_TABLE_READ);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
