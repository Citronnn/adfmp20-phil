package com.example.philsapp.api

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.philsapp.R
import java.io.File
import java.io.FileOutputStream

class Database(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    init {
        copyDataBase()
    }

    private fun copyDataBase() {
        File("${context.dataDir}/databases/").mkdirs()
        val path = "${context.dataDir}/databases/${DATABASE_NAME}"
        Log.i(TAG, "Copying Database")
        context.resources.openRawResource(R.raw.db).use { input ->
            FileOutputStream(path).use { output ->
                input.copyTo(output)
                output.flush()
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun getAllPhilosophers(): ArrayList<Philosopher> {
        val db = readableDatabase
        val data = ArrayList<Philosopher>()
        db.rawQuery("SELECT * FROM Philosopher", null).use { c ->
            if (c.moveToFirst()) {
                do {
                    data.add(
                        Philosopher(
                            wikiPageId = c.getInt(0),
                            abstract = c.getString(1),
                            gender = c.getString(2),
                            birthDate = c.getString(3),
                            deathDate = c.getString(4),
                            name = c.getString(5),
                            db = db
                        )
                    )
                } while (c.moveToNext())
            }
        }
        return data
    }

    companion object {
        var DATABASE_NAME = "db.sqlite"
        var DATABASE_VERSION = 1
        var TAG = "Database"
    }
}