package com.example.philsapp.api

import android.database.sqlite.SQLiteDatabase

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
) : WikiObject, DbAware

data class Philosopher(
    override val name: String,
    override val wikiPageId: Int?,
    override val abstract: String?,
    val gender: String?,
    val birthDate: String?,
    val deathDate: String?,
    override val db: SQLiteDatabase
) : WikiObject, DbAware
