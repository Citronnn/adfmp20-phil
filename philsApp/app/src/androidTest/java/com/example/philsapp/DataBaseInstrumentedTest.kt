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
        assertEquals(phil, db.getOnePhilosopher(ARISTOTLE_ID))
        assertNotNull(phil.name)

        val influenced = phil.influencedOn
        assertTrue(influenced.size > 0)
        assertTrue(influenced[0].wasInfluencedBy.any {
            it.wikiPageId == ARISTOTLE_ID
        })

        assertTrue(phil.notableIdeas.size > 0)
        assertTrue(phil.names.size > 0)
        assertTrue(phil.wasBorn.size > 0)
        assertTrue(phil.died.size > 0)
        assertTrue(phil.mainInterests.size > 0)
        assertTrue(phil.schools.size > 0)
        assertTrue(phil.eras.size > 0)
        assertTrue(phil.wikiPagePopularity > 0)
        assertNotNull(phil.nationalities)

        phil.notableIdeas.forEach { it ->
            assertTrue(it.philosophers.any {
                it.wikiPageId == ARISTOTLE_ID
            })
        }
        phil.mainInterests.forEach { it ->
            assertTrue(it.philosophers.any {
                it.wikiPageId == ARISTOTLE_ID
            })
        }
        phil.schools.forEach { it ->
            assertTrue(it.philosophers.any {
                it.wikiPageId == ARISTOTLE_ID
            })
        }
        phil.eras.forEach { it ->
            assertTrue(it.philosophers.any {
                it.wikiPageId == ARISTOTLE_ID
            })
        }
    }

    @Test
    fun testOtherStuff() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Database(context)

        val eras = db.getAllEras()
        assertTrue(eras.size > 0)
        assertEquals(eras[0], db.getOneEra(eras[0].name))

        val ideas = db.getAllIdeas()
        assertTrue(ideas.size > 0)
        assertEquals(ideas[0], db.getOneIdea(ideas[0].name))

        val schools = db.getAllSchools()
        assertTrue(schools.size > 0)
        assertEquals(schools[0], db.getOneSchool(schools[0].name))

        val interests = db.getAllInterests()
        assertTrue(interests.size > 0)
        assertEquals(interests[0], db.getOneInterest(interests[0].name))
    }
}
