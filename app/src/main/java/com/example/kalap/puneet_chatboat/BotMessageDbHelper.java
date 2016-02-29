package com.example.kalap.puneet_chatboat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BotMessageDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "botmsg";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CONTENT = "content";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_CONTENT + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BotApp.db";

    public BotMessageDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }
}
