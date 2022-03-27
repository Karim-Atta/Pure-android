package com.example.instabugandroidchallenge.countedwords.data.repository

import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import com.example.instabugandroidchallenge.countedwords.domain.model.ArrangingType
import com.example.instabugandroidchallenge.countedwords.domain.repository.FetchingCountedWordsRepo
import com.example.instabugandroidchallenge.countedwords.domain.sortedByRepeatedCount
import java.util.concurrent.CompletableFuture

class FakeCountedWordsRepository : FetchingCountedWordsRepo{
    private val remoteData = mutableListOf(
        CountedWord("second", 2),
        CountedWord("first", 1),
        CountedWord("third", 3)
    )

    private val storedData = mutableListOf(
        CountedWord("stored second", 2),
        CountedWord("stored first", 1),
        CountedWord("stored third", 3)
    )

    fun sortCountedWordsUseCase(countedWords: List<CountedWord>, arrangingType: ArrangingType): List<CountedWord>{
        return countedWords.sortedByRepeatedCount(arrangingType)
    }
    fun searchCountedWord(word: String, countedWords: List<CountedWord>): List<CountedWord>{
        return countedWords.filter { countedWord ->
            (countedWord.word.contains(word))
        }
    }
    override fun getCountedWords(
        isInternetConnected: Boolean
    ): CompletableFuture<List<CountedWord>> {
        return if (isInternetConnected) {
            CompletableFuture.completedFuture(remoteData)
        }else{
            return CompletableFuture.completedFuture(storedData)
        }
    }
}