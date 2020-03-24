package com.example.philsapp

import com.example.philsapp.api.dateToJd

import org.junit.Assert.assertEquals
import org.junit.Test

class UnitTestTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun julianDayTest() {
        val jd1 = dateToJd(-624, 12, 25)
        assertEquals(jd1.toInt(), 1493500)

        val jd2 = dateToJd(2000, 1, 1)
        assertEquals(jd2.toInt(), 2451544)
    }
}
