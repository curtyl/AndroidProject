package com.example.louis.androidproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.louis.androidproject.model.CityObject;
import com.example.louis.androidproject.model.GlobalObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 10/02/2017.
 */

public class DatabaseHandler implements Database {

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context pContext;

    public DatabaseHandler(Context pContext){
        this.pContext = pContext;
        mDbHelper = new DatabaseHelper(pContext);
    }

    public void insert(CityObject cObj) {
        mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, cObj.getIdx());
        values.put(COLUMN_NAME_CITY,cObj.getName());
        values.put(COLUMN_NAME_URL, cObj.getUrl());
        values.put(COLUMN_NAME_ID, cObj.getId());
        mDb.insert(VILLE_TABLE_NAME, null, values);
    }

    public List<CityObject> selectAll() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = new String[] {
                KEY_ID,
                COLUMN_NAME_CITY
        };
        //Cursor cursor = db.rawQuery("SELECT * FROM " + VILLE_TABLE_NAME + ";", projection);
        Cursor cursor = db.query(VILLE_TABLE_NAME, projection, null, null, null, null, null);

        List<CityObject> itemIds = new ArrayList<>();
        CityObject city;
        if(cursor.moveToFirst()) {
            do {
                city = new CityObject();
                city.setIdx(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                city.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CITY)));
                itemIds.add(city);
            } while(cursor.moveToNext());
        }
        return itemIds;
    }

    public CityObject selectId(int idx){

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = new String[] {
                KEY_ID,
                COLUMN_NAME_CITY
        };
        String selection = KEY_ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(idx)};

        Cursor cursor = db.query(VILLE_TABLE_NAME, projection,selection, selectionArgs, null, null, null);

        CityObject city = new CityObject();
        city.setIdx(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        city.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CITY)));

        return city;
    }

    public void removeObj(GlobalObject mObj) {
        mDb = mDbHelper.getWritableDatabase();
        mDb.delete(VILLE_TABLE_NAME, KEY_ID + "= ?", new String[]{String.valueOf(mObj.getRxs().getObs().get(0).getMsg().getCity().getIdx())});
    }

    public void close() {
        mDbHelper.close();
    }
}

