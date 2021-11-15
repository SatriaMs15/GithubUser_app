package com.example.consumerapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.consumerapp.database.DatabaseUser.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbuserfavorite"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseUser.UserColumns.USERNAME} TEXT PRIMARY KEY NOT NULL," +
                " ${DatabaseUser.UserColumns.AVATAR} TEXT NOT NULL," +
                " ${DatabaseUser.UserColumns._ID} INTEGER NOT NULL," +
                " ${DatabaseUser.UserColumns.COMPANY} TEXT NOT NULL,"+
                " ${DatabaseUser.UserColumns.FAVORITE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL(SQL_CREATE_TABLE_USER)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        }
        onCreate(db)
    }
}