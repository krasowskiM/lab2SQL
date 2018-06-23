package com.example.student.lab2sql.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "phones";
    public static final String ID = "_id";
    public static final String TABLE_NAME = "phones_table";
    public static final String COLUMN_1 = "brand";
    private static final String COLUMN_2 = "model";
    private static final String COLUMN_3 = "version";
    private static final String COLUMN_4 = "www";
    private static final String TB_CREATE = "CREATE TABLE " + TABLE_NAME
            + "(" + ID + " integer primary key autoincrement, " + COLUMN_1 + " text not NULL, "
            + COLUMN_2 + " text," + COLUMN_3 + " text," + COLUMN_4 + " text);";
    private static final String TB_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(TB_DROP);
        onCreate(sqLiteDatabase);
    }
}
