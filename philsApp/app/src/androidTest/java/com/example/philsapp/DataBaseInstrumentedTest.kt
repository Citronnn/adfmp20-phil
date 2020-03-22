package com.example.philsapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.philsapp.api.Database
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


const val ARISTOTLE_ID = 308


@RunWith(AndroidJUnit4::class)
class DataBaseInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.philsapp", appContext.packageName)
    }

    @Test
    fun testConnect() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Database(context)
        val data = db.getAllPhilosophers()
        data.forEach { it ->
            assertNotNull(it.name)
            assertNotNull(it.wikiPageId)
        }
    }

    @Test
    fun testPhilosopher() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Database(context)
        val phil = db.getOnePhilosopher(ARISTOTLE_ID)
        assertNotNull(phil.name)

        val influenced = phil.influencedOn
        assertTrue(influenced.size > 0)
        assertTrue(influenced[0].wasInfluencedBy.any {
            it.wikiPageId == ARISTOTLE_ID
        })

        assertTrue(phil.notableIdeas.size > 0)
    }
}
