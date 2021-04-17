package com.kumsal.voice_newspaper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbElements(context: Context, version: Int, dbName: String) : SQLiteOpenHelper(context, dbName, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        var create_Post_table = "CREATE table likes (" +
                "uid TEXT," +
                "pid TEXT" +
                ");"
        db?.execSQL(create_Post_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS likes");
        onCreate(db);
    }
}