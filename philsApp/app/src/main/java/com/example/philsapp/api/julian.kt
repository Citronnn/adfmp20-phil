package com.example.philsapp.api

import kotlin.math.truncate

fun dateToJd(year: Int, month: Int = 1, day: Int = 1): Double {
    val yearP = if (month == 1 || month == 2) year - 1 else year
    val monthP = if (month == 1 || month == 2) month + 12 else month
    val b: Double
    b = if ((year < 1582) ||
        (year == 1582 && month < 10) ||
        (year == 1582 && month == 10 && day < 15)
    ) {
        0.0
    } else {
        val a = truncate(yearP / 100.0)
        2.0 - a + truncate(a / 4.0)
    }
    val c = if (yearP < 0) truncate((365.25 * yearP) - 0.75) else truncate(365.25 * yearP)
    val d = truncate(30.6001 * (monthP + 1))
    return b + c + d + day + 1720994.5
}