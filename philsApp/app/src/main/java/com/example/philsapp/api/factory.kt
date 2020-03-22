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