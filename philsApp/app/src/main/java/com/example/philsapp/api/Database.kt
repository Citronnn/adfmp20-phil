package com.example.philsapp.api

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.philsapp.R
import java.io.FileOutputStream


class Database(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private fun copyDataBase(db: SQLiteDatabase) {
        Log.i(TAG, "Copying Database")
        context.resources.openRawResource(R.raw.db).use { input ->
            FileOutputStream(db.path).use { output ->
                input.copyTo(output)
                output.flush()
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let { copyDataBase(db) }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let { copyDataBase(db) }
    }

    fun getAllPhilosophers(): ArrayList<String> {
        val db = readableDatabase
        val data = ArrayList<String>()
        db.rawQuery("SELECT * FROM Philosopher", null).use { c ->
            if (c.moveToFirst()) {
                do {
                    data.add(c.getString(0))
                } while (c.moveToNext())

            }
        }
        return data
    }

    companion object {
        var DATABASE_NAME = "db.sqlite"
        var DATABASE_VERSION = 1
        var TAG = "Database"

        var createDB = false
        var upgradeDB = false
    }
}