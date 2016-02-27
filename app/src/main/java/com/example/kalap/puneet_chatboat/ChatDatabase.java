package com.example.kalap.puneet_chatboat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDatabase extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "messages";
    public static final String MESSAGE_ID = "messageid";
    public static final String MESSAGE = "message";
    public static final String MESSAGE_TYPE = "messagetype";
    public static final String TYPE_TEXT = " TEXT";
    public static final String COMMA = ",";
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
            MESSAGE_ID + " INTEGER PRIMARY KEY,"+
            MESSAGE + TYPE_TEXT + COMMA +
            MESSAGE_TYPE + TYPE_TEXT + " )";
    public static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String DATABASE_NAME = "chat.db";
    public static final int VERSION = 1;

    public ChatDatabase(Context context){
        super(context,DATABASE_NAME,null,VERSION);
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
