package com.example.instabugandroidchallenge.countedwords.domain.interactor

import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import com.example.instabugandroidchallenge.countedwords.domain.model.ArrangingType
import org.junit.Before

import org.junit.Test

class SortCountedWordsUseCaseTest {

    private lateinit var sortUseCase: SortCountedWordsUseCase
    @Before
    fun setup() {
        sortUseCase = SortCountedWordsUseCase()
    }

    @Test
    fun `sort counted list with ascending order`() {
        val countedWords = mutableListOf(
            CountedWord("second", 2),
            CountedWord("one", 1),
            CountedWord("third", 3)
        )
        val sortedList = sortUseCase(countedWords, ArrangingType.ASCENDING)
        assert(sortedList[0].word == "one")
    }

    @Test
    fun `sort counted list with descending order`() {
        val countedWords = mutableListOf(
            CountedWord("second", 2),
            CountedWord("one", 1),
            CountedWord("third", 3)
        )
        val sortedList = sortUseCase(countedWords, ArrangingType.DESCENDING)
        assert(sortedList[0].word == "third")
    }
}