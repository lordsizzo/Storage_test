package com.example.storage_test.model.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/**
 * 2.- sqlite the OpenHelper class
 */
class UserCredentials(private val context: Context, databaseName: String, version: Int) :
    SQLiteOpenHelper(context, databaseName, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS ${UserTable.TABLE_NAME} (" +
                    "${UserTable.COLUMN_FIRST} TEXT," +
                    "${UserTable.COLUMN_LAST} TEXT," +
                    "${UserTable.COLUMN_ADDRESS} TEXT," +
                    "${UserTable.COLUMN_PHONE} TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${UserTable.TABLE_NAME}")
    }

}