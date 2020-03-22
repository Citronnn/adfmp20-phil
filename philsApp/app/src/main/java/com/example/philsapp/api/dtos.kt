package com.example.philsapp.api

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull

interface DbAware {
    val db: SQLiteDatabase
}

interface WikiLink {
    val wikiPageId: Int?

    val wikipediaLink: String?
        get() = wikiPageId?.let { "https://en.wikipedia.org/?curid=$wikiPageId" }
}

interface WikiObject : WikiLink {
    val name: String
    val abstract: String?
}

data class MainInterest(
    override val name: String,
    override val wikiPageId: Int?,
    override val abstract: String?,
    override val db: SQLiteDatabase
) : WikiObject, DbAware

data class PhilosophicalSchool(
    override val name: String,
    override val wikiPageId: Int?,
    override val abstract: String?,
    override val db: SQLiteDatabase
) : WikiObject, DbAware

data class NotableIdea(
    override val name: String,
    override val wikiPageId: Int?,
    override val abstract: String?,
    override val db: SQLiteDatabase
) : WikiObject, DbAware {

    constructor(c: Cursor, db: SQLiteDatabase) : this(
        name = c.getString(0),
        abstract = c.getStringOrNull(1),
        wikiPageId = c.getIntOrNull(2),
        db = db
    )

    companion object {
        const val TABLE = "notableIdea"
        const val COLUMNS = "notableIdea, abstract, wikiPageID"
    }
}

data class Philosopher(
    override val name: String,
    override val wikiPageId: Int?,
    override val abstract: String?,
    val gender: String?,
    val birthDate: String?,
    val deathDate: String?,
    override val db: SQLiteDatabase
) : WikiObject, DbAware {
    constructor(c: Cursor, db: SQLiteDatabase) :
            this(
                wikiPageId = c.getInt(0),
                abstract = c.getString(1),
                gender = c.getStringOrNull(2),
                birthDate = c.getStringOrNull(3),
                deathDate = c.getStringOrNull(4),
                name = c.getString(5),
                db = db
            )

    val influencedOn: ArrayList<Philosopher> by lazy {
        val result = ArrayList<Philosopher>()
        db.rawQuery(
            """
            SELECT $COLUMNS FROM $TABLE WHERE wikiPageID IN (
                SELECT wikiPageID FROM $TABLE_INFLUENCED
                WHERE object1_wikiPageID = wikiPageID
            )
            """.trimIndent(), null
        ).use { c ->
            if (c.moveToFirst()) {
                do {
                    result.add(Philosopher(c, db))
                } while (c.moveToNext())
            }
        }
        result
    }

    val wasInfluencedBy: ArrayList<Philosopher> by lazy {
        val result = ArrayList<Philosopher>()
        db.rawQuery(
            """
            SELECT $COLUMNS FROM $TABLE WHERE wikiPageID IN (
                SELECT wikiPageID FROM $TABLE_INFLUENCED
                WHERE object2_wikiPageID = wikiPageID
            )
            """.trimIndent(), null
        ).use { c ->
            if (c.moveToFirst()) {
                do {
                    result.add(Philosopher(c, db))
                } while (c.moveToNext())
            }
        }
        result
    }

    val notableIdeas: ArrayList<NotableIdea> by lazy {
        val data = ArrayList<NotableIdea>()
        db.rawQuery(
            """
            SELECT ${NotableIdea.COLUMNS} FROM ${NotableIdea.TABLE}
            WHERE notableIdea IN (
                SELECT s.notableIdea FROM $TABLE_IDEAS s
                WHERE philosopher_wikiPageID = $wikiPageId
            )
        """.trimIndent(), null
        ).use { c ->
            if (c.moveToFirst()) {
                do {
                    data.add(NotableIdea(c, db))
                } while (c.moveToNext())
            }
        }
        data
    }

    companion object {
        const val COLUMNS = "wikiPageID, abstract, gender, birthDate, deathDate, name"
        const val TABLE = "Philosopher"
        const val TABLE_INFLUENCED = "PhilosopherInfluenced"
        const val TABLE_IDEAS = "PhilopherHasNotableIdea"
    }
}
