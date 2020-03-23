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

abstract class WikiObject : WikiLink, DbAware {
    abstract val name: String
    abstract val abstract: String?

    val wikiPagePopularity: Int by lazy {
        db.rawQuery(
            """
            SELECT total_visits FROM WikiPagePopularity
            WHERE wikiPageID = $wikiPageId
        """.trimIndent(), null
        ).use { c ->
            c.moveToFirst()
            c.getInt(0)
        }
    }
}

class EraFactory : DTOFactory<String, Era>() {
    override var saved: HashMap<String, Era> = HashMap()
    override fun create(c: Cursor, db: SQLiteDatabase): Era {
        return Era(c, db)
    }

    override fun getKey(c: Cursor): String {
        return c.getString(0)
    }
}

data class Era(
    override val name: String,
    override val abstract: String?,
    override val wikiPageId: Int?,
    override val db: SQLiteDatabase
) : WikiObject(), DbAware {
    constructor(c: Cursor, db: SQLiteDatabase) : this(
        name = c.getString(0),
        abstract = c.getStringOrNull(1),
        wikiPageId = c.getIntOrNull(2),
        db = db
    )

    val philosophers: ArrayList<Philosopher> by lazy {
        db.rawQuery(
            """
            SELECT ${Philosopher.COLUMNS} FROM ${Philosopher.TABLE}
            WHERE wikiPageID IN (
                SELECT philosopher_wikiPageID FROM ${Philosopher.TABLE_ERA}
                WHERE era = "$name"
            )
        """.trimIndent(), null
        ).use { c ->
            Philosopher.factory.getList(c, db)
        }
    }

    companion object {
        const val TABLE = "Era"
        const val COLUMNS = "era, abstract, wikiPageId"

        val factory = EraFactory()
    }
}

class MainInterestFactory : DTOFactory<String, MainInterest>() {
    override var saved: HashMap<String, MainInterest> = HashMap()
    override fun create(c: Cursor, db: SQLiteDatabase): MainInterest {
        return MainInterest(c, db)
    }

    override fun getKey(c: Cursor): String {
        return c.getString(0)
    }
}

data class MainInterest(
    override val name: String,
    override val abstract: String?,
    override val wikiPageId: Int?,
    override val db: SQLiteDatabase
) : WikiObject(), DbAware {
    constructor(c: Cursor, db: SQLiteDatabase) : this(
        name = c.getString(0),
        abstract = c.getStringOrNull(1),
        wikiPageId = c.getIntOrNull(2),
        db = db
    )

    val philosophers: ArrayList<Philosopher> by lazy {
        db.rawQuery(
            """
            SELECT ${Philosopher.COLUMNS} FROM ${Philosopher.TABLE}
            WHERE wikiPageID IN (
                SELECT philosopher_wikiPageID FROM ${Philosopher.TABLE_INTEREST}
                WHERE mainInterest = "$name"
            )
        """.trimIndent(), null
        ).use { c ->
            Philosopher.factory.getList(c, db)
        }
    }

    companion object {
        const val TABLE = "mainInterest"
        const val COLUMNS = "mainInterest, abstract, wikiPageId"

        val factory = MainInterestFactory()
    }
}

class SchoolFactory : DTOFactory<String, PhilosophicalSchool>() {
    override var saved: HashMap<String, PhilosophicalSchool> = HashMap()

    override fun create(c: Cursor, db: SQLiteDatabase): PhilosophicalSchool {
        return PhilosophicalSchool(c, db)
    }

    override fun getKey(c: Cursor): String {
        return c.getString(0)
    }

}

data class PhilosophicalSchool(
    override val name: String,
    override val wikiPageId: Int?,
    override val abstract: String?,
    override val db: SQLiteDatabase
) : WikiObject(), DbAware {
    constructor(c: Cursor, db: SQLiteDatabase) : this(
        name = c.getString(0),
        abstract = c.getStringOrNull(1),
        wikiPageId = c.getIntOrNull(2),
        db = db
    )

    val philosophers: ArrayList<Philosopher> by lazy {
        db.rawQuery(
            """
            SELECT ${Philosopher.COLUMNS} FROM ${Philosopher.TABLE}
            WHERE wikiPageID IN (
                SELECT philosopher_wikiPageID FROM ${Philosopher.TABLE_SCHOOLS}
                WHERE philosophicalSchool = "$name"
            )
        """.trimIndent(), null
        ).use { c ->
            Philosopher.factory.getList(c, db)
        }
    }

    companion object {
        const val TABLE = "PhilosophicalSchool"
        const val COLUMNS = "philosophicalSchool, abstract, wikiPageID"

        val factory = SchoolFactory()
    }
}

class PlaceFactory : DTOFactory<String, Place>() {
    override var saved: HashMap<String, Place> = HashMap()
    override fun create(c: Cursor, db: SQLiteDatabase): Place {
        return Place(c, db)
    }

    override fun getKey(c: Cursor): String {
        return c.getString(0)
    }
}

data class Place(
    override val wikiPageId: Int?,
    val name: String,
    override val db: SQLiteDatabase
) : WikiLink, DbAware {
    constructor(c: Cursor, db: SQLiteDatabase) : this(
        name = c.getString(0),
        wikiPageId = c.getIntOrNull(1),
        db = db
    )

    companion object {
        const val BIRTH_TABLE = "PhilosopherWasBorn"
        const val DEATH_TABLE = "PhilosopherDied"
        const val BIRTH_COLUMNS = "birthPlace, birthPlace_wikiPageID"
        const val DEATH_COLUMNS = "deathPlace, deathPlace_wikiPageID"

        val factory = PlaceFactory()
    }
}

class NotableIdeaFactory : DTOFactory<String, NotableIdea>() {
    override var saved: HashMap<String, NotableIdea> = HashMap()

    override fun create(c: Cursor, db: SQLiteDatabase): NotableIdea {
        return NotableIdea(c, db)
    }

    override fun getKey(c: Cursor): String {
        return c.getString(0)
    }
}

data class NotableIdea(
    override val name: String,
    override val wikiPageId: Int?,
    override val abstract: String?,
    override val db: SQLiteDatabase
) : WikiObject() {
    constructor(c: Cursor, db: SQLiteDatabase) : this(
        name = c.getString(0),
        abstract = c.getStringOrNull(1),
        wikiPageId = c.getIntOrNull(2),
        db = db
    )

    val philosophers: ArrayList<Philosopher> by lazy {
        db.rawQuery(
            """
            SELECT ${Philosopher.COLUMNS} FROM ${Philosopher.TABLE}
            WHERE wikiPageID IN (
                SELECT philosopher_wikiPageID FROM ${Philosopher.TABLE_IDEAS}
                WHERE notableIdea = "$name"
            )
        """.trimIndent(), null
        ).use { c ->
            Philosopher.factory.getList(c, db)
        }
    }

    companion object {
        const val TABLE = "notableIdea"
        const val COLUMNS = "notableIdea, abstract, wikiPageID"

        val factory = NotableIdeaFactory()
    }
}

class PhilosopherFactory : DTOFactory<Int, Philosopher>() {
    override var saved: HashMap<Int, Philosopher> = HashMap()

    override fun create(c: Cursor, db: SQLiteDatabase): Philosopher {
        return Philosopher(c, db)
    }

    override fun getKey(c: Cursor): Int {
        return c.getInt(0)
    }
}

data class Philosopher(
    override val name: String,
    override val wikiPageId: Int,
    override val abstract: String?,
    val gender: String?,
    val birthDate: String?,
    val deathDate: String?,
    override val db: SQLiteDatabase
) : WikiObject() {
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
        db.rawQuery(
            """
            SELECT $COLUMNS FROM $TABLE WHERE wikiPageID IN (
                SELECT wikiPageID FROM $TABLE_INFLUENCED
                WHERE object1_wikiPageID = wikiPageID
            )
            """.trimIndent(), null
        ).use { c ->
            factory.getList(c, db)
        }
    }

    val wasInfluencedBy: ArrayList<Philosopher> by lazy {
        db.rawQuery(
            """
            SELECT $COLUMNS FROM $TABLE WHERE wikiPageID IN (
                SELECT wikiPageID FROM $TABLE_INFLUENCED
                WHERE object2_wikiPageID = wikiPageID
            )
            """.trimIndent(), null
        ).use { c ->
            factory.getList(c, db)
        }
    }

    val notableIdeas: ArrayList<NotableIdea> by lazy {
        db.rawQuery(
            """
            SELECT ${NotableIdea.COLUMNS} FROM ${NotableIdea.TABLE}
            WHERE notableIdea IN (
                SELECT s.notableIdea FROM $TABLE_IDEAS s
                WHERE philosopher_wikiPageID = $wikiPageId
            )
        """.trimIndent(), null
        ).use { c ->
            NotableIdea.factory.getList(c, db)
        }
    }

    val names: ArrayList<String> by lazy {
        val data: ArrayList<String> = ArrayList()
        db.rawQuery(
            """
            SELECT name FROM $TABLE_NAMES
            WHERE wikiPageID = $wikiPageId
        """.trimIndent(), null
        ).use { c ->
            if (c.moveToFirst()) {
                do {
                    data.add(c.getString(0))
                } while (c.moveToNext())
            }
        }
        data
    }

    val nationalities: ArrayList<String> by lazy {
        val data: ArrayList<String> = ArrayList()
        db.rawQuery(
            """
            SELECT nationality FROM $TABLE_NATIONALITIES
            WHERE wikiPageID = $wikiPageId
        """.trimIndent(), null
        ).use { c ->
            if (c.moveToFirst()) {
                do {
                    data.add(c.getString(0))
                } while (c.moveToNext())
            }
        }
        data
    }

    val wasBorn: ArrayList<Place> by lazy {
        db.rawQuery(
            """
            SELECT ${Place.BIRTH_COLUMNS} FROM ${Place.BIRTH_TABLE}
            WHERE wikiPageID = $wikiPageId
        """.trimIndent(), null
        ).use { c ->
            Place.factory.getList(c, db)
        }
    }

    val died: ArrayList<Place> by lazy {
        db.rawQuery(
            """
            SELECT ${Place.DEATH_COLUMNS} FROM ${Place.DEATH_TABLE}
            WHERE wikiPageID = $wikiPageId
        """.trimIndent(), null
        ).use { c ->
            Place.factory.getList(c, db)
        }
    }

    val schools: ArrayList<PhilosophicalSchool> by lazy {
        db.rawQuery(
            """
            SELECT ${PhilosophicalSchool.COLUMNS} FROM ${PhilosophicalSchool.TABLE}
            WHERE philosophicalSchool IN (
                SELECT philosophicalSchool FROM $TABLE_SCHOOLS
                WHERE philosopher_wikiPageID = $wikiPageId
            )
        """.trimIndent(), null
        ).use { c ->
            PhilosophicalSchool.factory.getList(c, db)
        }
    }

    val mainInterests: ArrayList<MainInterest> by lazy {
        db.rawQuery(
            """
            SELECT ${MainInterest.COLUMNS} FROM ${MainInterest.TABLE}
            WHERE mainInterest IN (
                SELECT mainInterest FROM $TABLE_INTEREST
                WHERE philosopher_wikiPageID = $wikiPageId
            )
        """.trimIndent(), null
        ).use { c ->
            MainInterest.factory.getList(c, db)
        }
    }

    val eras: ArrayList<Era> by lazy {
        db.rawQuery(
            """
            SELECT ${Era.COLUMNS} FROM ${Era.TABLE}
            WHERE era IN (
                SELECT era FROM $TABLE_ERA
                WHERE philosopher_wikiPageID = $wikiPageId
            )
        """.trimIndent(), null
        ).use { c ->
            Era.factory.getList(c, db)
        }
    }

    companion object {
        const val COLUMNS = "wikiPageID, abstract, gender, date(birthDate), date(deathDate), name"
        const val TABLE = "Philosopher"
        const val TABLE_INFLUENCED = "PhilosopherInfluenced"
        const val TABLE_IDEAS = "PhilopherHasNotableIdea"
        const val TABLE_NAMES = "PhilosopherHasName"
        const val TABLE_SCHOOLS = "PhilopherHasPhilosophicalSchool"
        const val TABLE_INTEREST = "PhilopherHasMainInterest"
        const val TABLE_ERA = "PhilopherHasEra"
        const val TABLE_NATIONALITIES = "PhilosopherHasNationality"
        val factory = PhilosopherFactory()
    }
}
