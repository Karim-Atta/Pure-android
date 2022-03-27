package com.example.instabugandroidchallenge.countedwords.presentation.di

import com.example.instabugandroidchallenge.countedwords.data.repository.FakeCountedWordsRepository
import com.example.instabugandroidchallenge.countedwords.domain.interactor.*
import com.example.instabugandroidchallenge.countedwords.domain.repository.FetchingCountedWordsRepo

class FakeCountedWordsContainer() {

    val fetchCountedWordsRepository : FetchingCountedWordsRepo = FakeCountedWordsRepository()
    private val fetchCountedWordsUseCase = FetchInstabugCountedWordsUseCase(fetchCountedWordsRepository)
    val searchForWordUseCase = SearchForWordUseCase()
    val sortCountedUseCase = SortCountedWordsUseCase()
    val countedWordUseCases = CountedWordsUseCases(
        fetchCountedWordsUseCase,
        sortCountedUseCase,
        searchForWordUseCase
    )
}