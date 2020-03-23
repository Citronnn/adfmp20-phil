package com.example.philsapp.api

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

abstract class DTOFactory<Key, T> {
    abstract var saved: HashMap<Key, T>

    protected abstract fun create(c: Cursor, db: SQLiteDatabase): T

    protected abstract fun getKey(c: Cursor): Key

    fun createOrGet(c: Cursor, db: SQLiteDatabase): T {
        val key = getKey(c)
        if (!saved.containsKey(key)) {
            saved[key] = create(c, db)
        }
        return saved[key]!!
    }

    fun tryGet(key: Key): T? {
        return saved[key]
    }

    fun getOrSql(key: Key, sql: String, db: SQLiteDatabase): T {
        tryGet(key)?.let { return it }
        db.rawQuery(sql, null).use { c ->
            c.moveToFirst()
            return createOrGet(c, db)
        }
    }

    fun getList(c: Cursor, db: SQLiteDatabase): ArrayList<T> {
        val data = ArrayList<T>()
        if (c.moveToFirst()) {
            do {
                data.add(createOrGet(c, db))
            } while (c.moveToNext())
        }
        return data
    }
}