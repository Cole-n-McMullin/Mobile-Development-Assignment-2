package com.example.assignment2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.assignment2.Place;

public class DBMgr {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBMgr(Context c) {
        context = c;
    }

    public DBMgr open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(Place place) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.ADDRESS, place.getAddress());
        contentValue.put(DatabaseHelper.LATITUDE, place.getLatitude());
        contentValue.put(DatabaseHelper.LONGITUDE, place.getLongitude());
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch(String address) {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.ADDRESS, DatabaseHelper.LATITUDE, DatabaseHelper.LONGITUDE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, DatabaseHelper.ADDRESS + " = " + address, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetchAll() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.ADDRESS, DatabaseHelper.LATITUDE, DatabaseHelper.LONGITUDE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String address, double latitude, double longitude) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ADDRESS, address);
        contentValues.put(DatabaseHelper.LATITUDE, latitude);
        contentValues.put(DatabaseHelper.LONGITUDE, longitude);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}
