package cn.xavier.movie.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.xavier.movie.db.DbConstant;
import cn.xavier.movie.db.DbOpenHelper;

/**
 * Created by yangxh on 17/7/30.
 */

public class NewDao {
    private final DbOpenHelper helper;

    public NewDao(Context context){
        helper= DbOpenHelper.getInstance(context);
    }

    public void insertReadRow(String id){
        SQLiteDatabase db= helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbConstant.DB_TABLE_READ_ID,id);
        db.insert(DbConstant.DB_TABLE_READ,null,contentValues);
    }

    public List<String> getAllReadColumns(){
        List<String> rList=new ArrayList<>();
        SQLiteDatabase db=helper.getReadableDatabase();
        String sqlQuery="select * from "+DbConstant.DB_TABLE_READ;
        Cursor cursor=db.rawQuery(sqlQuery,null);

        if(cursor.moveToFirst()){
            do {
                String id=cursor.getString(0);
                rList.add(id);
            }while (cursor.moveToNext());
        }

       return rList;
    }
}
