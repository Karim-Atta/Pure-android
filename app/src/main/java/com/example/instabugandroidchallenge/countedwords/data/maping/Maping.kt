package com.example.instabugandroidchallenge.countedwords.data.maping

import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord

fun HashMap<String, Int>.toCountedWords(): List<CountedWord> {
    val countedWords = mutableListOf<CountedWord>()
    this.keys.forEach { word ->
        if (word.isNotEmpty()) {
            this[word]?.let { repeatedCount ->
                countedWords.add(CountedWord(word, repeatedCount))
            }
        }
    }
    return countedWords
}