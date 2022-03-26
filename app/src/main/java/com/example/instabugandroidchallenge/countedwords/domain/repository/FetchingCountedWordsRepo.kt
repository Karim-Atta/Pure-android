package com.example.instabugandroidchallenge.countedwords.domain.repository

import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import java.util.concurrent.CompletableFuture

interface FetchingCountedWordsRepo {
    fun getCountedWords(
        isInternetConnected: Boolean
    ): CompletableFuture<List<CountedWord>>
}