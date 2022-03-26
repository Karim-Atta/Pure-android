package com.example.instabugandroidchallenge.countedwords.data.source.local.contract

import android.provider.BaseColumns

object InstabugContract {
    object CountedWords : BaseColumns {
        const val TABLE_NAME = "counted_words"
        const val COLUMN_NAME_WORD = "word"
        const val COLUMN_NAME_COUNT = "count"
    }
}