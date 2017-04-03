package com.example.louis.androidproject.database;


/**
 * Created by louis on 10/02/2017 for AndroidProject.
 */

interface Database {

    String VILLE_TABLE_NAME = "ville";
    String COLUMN_NAME_CITY = "city";
    String COLUMN_NAME_URL = "url";
    String COLUMN_NAME_ID = "id";
    String KEY_ID = "idx";

    String CREATE_VILLE_TABLE =
            "CREATE TABLE " + VILLE_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_CITY + " TEXT," +
                    COLUMN_NAME_URL + " TEXT," +
                    COLUMN_NAME_ID + " TEXT)";

    String DELETE_VILLE_TABLE =
            "DROP TABLE IF EXISTS " + VILLE_TABLE_NAME;
}

