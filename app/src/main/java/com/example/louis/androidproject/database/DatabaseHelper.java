package com.example.louis.androidproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by louis on 10/02/2017 for AndroidProject.
 */

class DatabaseHelper extends SQLiteOpenHelper implements Database{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ville.db";
    public SQLiteDatabase mDb = null;

    /**
     * The constructor of the class
     * @param context context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create the database
     * @param db Database
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_VILLE_TABLE);
    }

    /**
     * Upgrade the database
     * @param db Database
     * @param oldVersion integer
     * @param newVersion integer
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This Database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DELETE_VILLE_TABLE);
        onCreate(db);
    }

    /**
     * Restore an old version of the database
     * @param db Database
     * @param oldVersion integer
     * @param newVersion integer
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}