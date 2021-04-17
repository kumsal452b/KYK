package com.kumsal.voice_newspaper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbElements(context: Context, version: Int, dbName: String) : SQLiteOpenHelper(context, dbName, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        var create_Post_table = "CREATE table news (content TEXT, " +
                "url TEXT," +
                "image TEXT," +
                "title TEXT," +
                "name TEXT," +
                "time TEXT" +
                ");"
        db?.execSQL(create_Post_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS news");
        onCreate(db);
    }
}