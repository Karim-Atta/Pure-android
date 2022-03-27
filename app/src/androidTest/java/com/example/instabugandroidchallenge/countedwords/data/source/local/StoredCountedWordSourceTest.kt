package com.example.instabugandroidchallenge.countedwords.data.source.local

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.instabugandroidchallenge.countedwords.data.source.local.helper.InstabugSQLHelper
import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StoredCountedWordSourceTest{

    var appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    lateinit var dbHelper: InstabugSQLHelper // Your DbHelper class
    lateinit var localDataSource: StoredCountedWordSource
    @Before
    fun setup() {
        dbHelper = InstabugSQLHelper(appContext)
        localDataSource = StoredCountedWordSource(dbHelper.writableDatabase, dbHelper.readableDatabase)
        dbHelper.clearDbAndRecreate()
    }

    @Test
    fun testDbInsertion() {

        // Given
        val countedWord = CountedWord("word", 2)

        // When
        localDataSource.insertCountedWord(countedWord)

        // Then
        assertEquals(localDataSource.getCountedWords()[0], countedWord)
    }

    @Test
    fun testDbDeletion() {

        localDataSource.deleteCountedWords()

        // Then
        assertEquals(localDataSource.getCountedWords(), mutableListOf<CountedWord>())
    }

    @After
    fun tearDown() {
        dbHelper.clearDbAndRecreate()
    }
}