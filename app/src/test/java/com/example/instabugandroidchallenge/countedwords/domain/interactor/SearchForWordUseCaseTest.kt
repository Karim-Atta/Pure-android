package com.example.instabugandroidchallenge.countedwords.domain.interactor

import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import org.junit.Before
import org.junit.Test

class SearchForWordUseCaseTest{

    private lateinit var filterUseCase: SearchForWordUseCase
    @Before
    fun setup() {
        filterUseCase = SearchForWordUseCase()
    }

    @Test
    fun `filter with word should return counted items that contain that word`(){
        val word = "th"
        val countedWords = mutableListOf(
            CountedWord("theme", 1),
            CountedWord("the", 2),
            CountedWord("instabug", 3)
        )
        val countedWordsAfterFilteration = mutableListOf(
            CountedWord("theme", 1),
            CountedWord("the", 2),
        )
        val filteredList = filterUseCase.invoke(word, countedWords)
        assert(filteredList == countedWordsAfterFilteration)
    }

    @Test
    fun `filter with empty word should return original list`(){
        val word = ""
        val countedWords = mutableListOf(
            CountedWord("theme", 1),
            CountedWord("the", 2),
            CountedWord("instabug", 3)
        )
        val filteredList = filterUseCase.invoke(word, countedWords)
        assert(filteredList == countedWords)
    }
}