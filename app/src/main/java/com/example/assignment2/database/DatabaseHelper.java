package com.example.assignment2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "NOTES";

    // Table columns
    public static final String _ID = "_id";
    public static final String ADDRESS = "address";
    public static final String LATITUDE = "latitude";

    public static final String LONGITUDE = "longitude";

    // Database Information
    static final String DB_NAME = "JOURNALDEV_NOTES.DB";

    // database version
    static final int DB_VERSION = 2;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ADDRESS + " TEXT NOT NULL, " + LATITUDE + " DOUBLE, " + LONGITUDE + " DOUBLE);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
