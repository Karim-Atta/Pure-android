package com.example.instabugandroidchallenge.countedwords.data.source.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.instabugandroidchallenge.countedwords.data.source.local.contract.InstabugContract.CountedWords
import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord

class StoredCountedWordSource constructor(
    private val instabugWritableDB: SQLiteDatabase,
    private val instabugReadableDB: SQLiteDatabase) {

    fun insertCountedWord(countedWord: CountedWord){
        val countedWordValues = ContentValues().apply {
            put(CountedWords.COLUMN_NAME_WORD, countedWord.word)
            put(CountedWords.COLUMN_NAME_COUNT, countedWord.count)
        }
        instabugWritableDB.insert(CountedWords.TABLE_NAME, null, countedWordValues)
    }

    fun deleteCountedWords(){
        instabugWritableDB.delete(
            CountedWords.TABLE_NAME,
            null,
            null,
        )
    }
    fun getCountedWords(): List<CountedWord>{

        val projection = arrayOf(CountedWords.COLUMN_NAME_WORD, CountedWords.COLUMN_NAME_COUNT)

        val cursor = instabugReadableDB.query(
            CountedWords.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val countedWords = mutableListOf<CountedWord>()
        with(cursor) {
            while (moveToNext()) {
                val word = (getColumnIndex(CountedWords.COLUMN_NAME_WORD).let {
                    cursor.getString(it)
                })
                val count =  (getColumnIndex(CountedWords.COLUMN_NAME_COUNT).let {
                    cursor.getInt(it)
                })
                countedWords.add(CountedWord(word, count))
            }
        }
        cursor.close()
        return countedWords.toList()
    }
}