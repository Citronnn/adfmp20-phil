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
        if (!DB_COPIED) {
            copyDataBase()
            DB_COPIED = true
        }
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

    fun getAllPhilosophers(filter: Filter? = null): ArrayList<Philosopher> {
        val db = readableDatabase
        db.rawQuery("SELECT ${Philosopher.COLUMNS} FROM Philosopher ${filter?.toSql() ?: ""}", null)
            .use { c ->
            return Philosopher.factory.getList(c, db)
        }
    }

    fun getOnePhilosopher(wikiPageId: Int): Philosopher {
        val db = readableDatabase
        return Philosopher.factory.getOrSql(
            wikiPageId,
            "SELECT ${Philosopher.COLUMNS} FROM Philosopher WHERE wikiPageId = $wikiPageId",
            db
        )
    }

    fun getAllSchools(filter: Filter? = null): ArrayList<PhilosophicalSchool> {
        val db = readableDatabase
        db.rawQuery(
            "SELECT ${PhilosophicalSchool.COLUMNS} FROM ${PhilosophicalSchool.TABLE} ${filter?.toSql()
                ?: ""}", null
        )
            .use { c ->
                return PhilosophicalSchool.factory.getList(c, db)
            }
    }

    fun getOneSchool(name: String): PhilosophicalSchool {
        val db = readableDatabase
        return PhilosophicalSchool.factory.getOrSql(
            name,
            "SELECT ${PhilosophicalSchool.COLUMNS} FROM ${PhilosophicalSchool.TABLE} WHERE philosophicalSchool = $name",
            db
        )
    }

    fun getAllIdeas(filter: Filter? = null): ArrayList<NotableIdea> {
        val db = readableDatabase
        db.rawQuery(
            "SELECT ${NotableIdea.COLUMNS} FROM ${NotableIdea.TABLE} ${filter?.toSql() ?: ""}", null
        )
            .use { c ->
                return NotableIdea.factory.getList(c, db)
            }
    }

    fun getOneIdea(name: String): NotableIdea {
        val db = readableDatabase
        return NotableIdea.factory.getOrSql(
            name,
            "SELECT ${NotableIdea.COLUMNS} FROM ${NotableIdea.TABLE} WHERE notableIdea = $name",
            db
        )
    }

    fun getAllInterests(filter: Filter? = null): ArrayList<MainInterest> {
        val db = readableDatabase
        db.rawQuery(
            "SELECT ${MainInterest.COLUMNS} FROM ${MainInterest.TABLE} ${filter?.toSql() ?: ""}",
            null
        )
            .use { c ->
                return MainInterest.factory.getList(c, db)
            }
    }


    fun getOneInterest(name: String): MainInterest {
        val db = readableDatabase
        return MainInterest.factory.getOrSql(
            name,
            "SELECT ${MainInterest.COLUMNS} FROM ${MainInterest.TABLE} WHERE mainInterest = $name",
            db
        )
    }

    fun getAllEras(filter: Filter? = null): ArrayList<Era> {
        val db = readableDatabase
        db.rawQuery("SELECT ${Era.COLUMNS} FROM ${Era.TABLE} ${filter?.toSql() ?: ""}", null)
            .use { c ->
                return Era.factory.getList(c, db)
            }
    }

    fun getOneEra(name: String): Era {
        val db = readableDatabase
        return Era.factory.getOrSql(
            name,
            "SELECT ${Era.COLUMNS} FROM ${Era.TABLE} WHERE era = $name",
            db
        )
    }

    companion object {
        var DATABASE_NAME = "db.sqlite"
        var DATABASE_VERSION = 1
        var TAG = "Database"

        var DB_COPIED = false
    }
}