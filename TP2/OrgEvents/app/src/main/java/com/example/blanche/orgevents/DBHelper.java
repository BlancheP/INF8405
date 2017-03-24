package com.example.blanche.orgevents;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void addUser(String username, String password) {
        String strSQL = "INSERT INTO " + PROFILE_TABLE_NAME + " VALUES (0, " + username + ", " + password + ", '')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL);
    }

    public void updateGroup(String groupname) {
        String strSQL = "UPDATE " + PROFILE_TABLE_NAME + " SET " + PROFILE_COLUMN_GROUP + " = " + groupname + " WHERE " + PROFILE_COLUMN_ID + " = 0";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL);
    }

    public void getCurrentUser() {
        String strSQL = "SELECT " + PROFILE_COLUMN_USERNAME + " FROM " + PROFILE_TABLE_NAME + " WHERE _ID = 0";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL);
    }

    public void getCurrentGroup() {
        String strSQL = "SELECT " + PROFILE_COLUMN_GROUP + " FROM " + PROFILE_TABLE_NAME + " WHERE _ID = 0";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL);
    }

    public void removeCurrentUser() {
        String strSQL = "REMOVE FROM " + PROFILE_TABLE_NAME + " WHERE _id = 0";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSQL);
    }

}
