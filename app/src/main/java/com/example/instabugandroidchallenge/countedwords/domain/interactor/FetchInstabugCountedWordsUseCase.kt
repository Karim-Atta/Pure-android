package com.example.instabugandroidchallenge.countedwords.domain.interactor

import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import com.example.instabugandroidchallenge.countedwords.domain.repository.FetchingCountedWordsRepo
import java.util.concurrent.CompletableFuture

class FetchInstabugCountedWordsUseCase constructor(private val repository: FetchingCountedWordsRepo) {

    operator fun invoke(isInternetConnected : Boolean): CompletableFuture<List<CountedWord>> {
        return repository.getCountedWords(isInternetConnected)
    }
}
