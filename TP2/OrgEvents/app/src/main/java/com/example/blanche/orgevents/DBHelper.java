package com.example.blanche.orgevents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Blanche on 3/22/2017.
 */

public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "MyDatabase.db";
    private static final int DATABASE_VERSION = 1;
    public static final String PROFILE_TABLE_NAME = "profile";
    public static final String PROFILE_COLUMN_ID = "_id";
    public static final String PROFILE_COLUMN_USERNAME = "username";
    public static final String PROFILE_COLUMN_PASSWORD = "password";
    public static final String PROFILE_COLUMN_GROUP = "groupname";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLE_CREATE = "CREATE TABLE " + PROFILE_TABLE_NAME + "(" +
                PROFILE_COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                PROFILE_COLUMN_USERNAME + " TEXT, " +
                PROFILE_COLUMN_PASSWORD + " TEXT, " +
                PROFILE_COLUMN_GROUP + " TEXT)";
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE_NAME);
        onCreate(db);
    }

}
