package com.example.philsapp

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.example.philsapp.api.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

const val ARISTOTLE_ID = 308


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class DataBaseTest {
    private lateinit var db: Database

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Database(context)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val context = ApplicationProvider.getApplicationContext<Context>()
        Assert.assertEquals("com.example.philsapp", context.packageName)
    }

    @Test
    fun testConnect() {
        val data = db.getAllPhilosophers()
        data.forEach { it ->
            Assert.assertNotNull(it.name)
            Assert.assertNotNull(it.wikiPageId)
        }
    }

    @Test
    fun testPhilosopher() {
        val phil = db.getOnePhilosopher(ARISTOTLE_ID)
        Assert.assertEquals(phil, db.getOnePhilosopher(ARISTOTLE_ID))
        Assert.assertNotNull(phil.name)

        val influenced = phil.influencedOn
        Assert.assertTrue(influenced.size > 0)
        Assert.assertTrue(influenced[0].wasInfluencedBy.any {
            it.wikiPageId == ARISTOTLE_ID
        })

        Assert.assertTrue(phil.notableIdeas.size > 0)
        Assert.assertTrue(phil.names.size > 0)
        Assert.assertTrue(phil.wasBorn.size > 0)
        Assert.assertTrue(phil.died.size > 0)
        Assert.assertTrue(phil.mainInterests.size > 0)
        Assert.assertTrue(phil.schools.size > 0)
        Assert.assertTrue(phil.eras.size > 0)
        Assert.assertTrue(phil.wikiPagePopularity > 0)
        Assert.assertNotNull(phil.nationalities)

        phil.notableIdeas.forEach { it ->
            Assert.assertTrue(it.philosophers.any {
                it.wikiPageId == ARISTOTLE_ID
            })
            Assert.assertNotNull(it.firstPhilosopher)
        }
        phil.mainInterests.forEach { it ->
            Assert.assertTrue(it.philosophers.any {
                it.wikiPageId == ARISTOTLE_ID
            })
            Assert.assertNotNull(it.firstPhilosopher)
        }
        phil.schools.forEach { it ->
            Assert.assertTrue(it.philosophers.any {
                it.wikiPageId == ARISTOTLE_ID
            })
            Assert.assertNotNull(it.firstPhilosopher)
        }
        phil.eras.forEach { it ->
            Assert.assertTrue(it.philosophers.any {
                it.wikiPageId == ARISTOTLE_ID
            })
            Assert.assertNotNull(it.firstPhilosopher)
        }
    }

    @Test
    fun testOtherStuff() {
        val eras = db.getAllEras()
        Assert.assertTrue(eras.size > 0)
        Assert.assertEquals(eras[0], db.getOneEra(eras[0].name))

        val ideas = db.getAllIdeas()
        Assert.assertTrue(ideas.size > 0)
        Assert.assertEquals(ideas[0], db.getOneIdea(ideas[0].name))

        val schools = db.getAllSchools()
        Assert.assertTrue(schools.size > 0)
        Assert.assertEquals(schools[0], db.getOneSchool(schools[0].name))

        val interests = db.getAllInterests()
        Assert.assertTrue(interests.size > 0)
        Assert.assertEquals(interests[0], db.getOneInterest(interests[0].name))
    }

    @Test
    fun testFilter() {
        Assert.assertEquals(db.getAllPhilosophers(Filter(limit = 10)).size, 10)
        Assert.assertEquals(db.getAllPhilosophers(Filter(limit = 15, offset = 10)).size, 15)
        Assert.assertTrue(
            db.getAllPhilosophers(
                Filter(
                    order = arrayOf(
                        OrderBy(
                            "name",
                            Order.ASC
                        )
                    )
                )
            ).size > 0
        )
        Assert.assertTrue(
            db.getAllPhilosophers(
                Filter(
                    order = arrayOf(
                        OrderBy("name", Order.ASC),
                        OrderBy("birthDate", Order.DESC)
                    )
                )
            ).size > 0
        )

        Assert.assertTrue(
            db.getAllPhilosophers(
                Filter(filter = arrayOf(FilterBy("name", Operator.CONTAINS, "Aristotle")))
            ).size > 0
        )
        Assert.assertEquals(
            db.getAllPhilosophers(
                Filter(filter = arrayOf(FilterBy("name", Operator.CONTAINS, "Vladimir Ivanov")))
            ).size, 0
        )
        Assert.assertEquals(
            db.getAllPhilosophers(
                Filter(filter = arrayOf(FilterBy("birthDate", Operator.GT, dateToJd(3000))))
            ).size, 0
        )
        Assert.assertEquals(
            db.getAllPhilosophers(
                Filter(
                    filter = arrayOf(
                        FilterBy("birthDate", Operator.LT, dateToJd(5000)),
                        FilterBy("birthDate", Operator.GT, dateToJd(-5000))
                    )
                )
            ).size,
            db.getAllPhilosophers(
                Filter(filter = arrayOf(FilterBy("birthDate", Operator.NOTNULL)))
            ).size
        )
    }
}
