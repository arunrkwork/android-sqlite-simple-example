package com.agri.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.DimenRes;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "Student.db";
    private static final String TABLE_NAME = "student_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "NAME";
    private static final String COL_3 = "SURNAME";
    private static final String COL_4 = "MARKS";

    private static final String QUERY_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_2 + " TEXT, "  + COL_3 + " TEXT, " + COL_4 + " INTEGER)";
    private static final String QUERY_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String QUERY_SELECT_TABLE = "SELECT * FROM " + TABLE_NAME;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.d(TAG, "DatabaseHelper: called");
       // SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: ");
        db.execSQL(QUERY_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: called");
        db.execSQL(QUERY_DROP_TABLE);
    }

    public boolean insertData(String name, String surName, String mark) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2, name);
        cv.put(COL_3, surName);
        cv.put(COL_4, mark);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) return false;
        else return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(QUERY_SELECT_TABLE, null);
        return res;
    }

    public boolean updateData(String id, String name, String surName, String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, id);
        cv.put(COL_2, name);
        cv.put(COL_3, surName);
        cv.put(COL_4, marks);
        int result = db.update(TABLE_NAME, cv, COL_1 + " = ?", new String[] { id });
        Log.d(TAG, "updateData: " + result);
        // return 1 when its updated else return 0
        return true;
    }

    public int deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COL_1 + " = ?", new String[] { id });
        Log.d(TAG, "deleteData: " + result);
        return result;
    }
}
