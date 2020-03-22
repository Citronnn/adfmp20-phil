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
        db.rawQuery("SELECT ${Philosopher.COLUMNS} FROM Philosopher", null).use { c ->
            return Philosopher.factory.getList(c, db)
        }
    }

    fun getOnePhilosopher(wikiPageId: Int): Philosopher {
        val db = readableDatabase
        db.rawQuery(
            "SELECT ${Philosopher.COLUMNS} FROM Philosopher WHERE wikiPageId = $wikiPageId", null
        ).use { c ->
            c.moveToFirst()
            return Philosopher.factory.createOrGet(c, db)
        }
    }

    companion object {
        var DATABASE_NAME = "db.sqlite"
        var DATABASE_VERSION = 1
        var TAG = "Database"
    }
}