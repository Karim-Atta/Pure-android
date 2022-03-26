package com.example.instabugandroidchallenge.countedwords.domain.interactor

data class CountedWordsUseCases constructor(
    val fetchCountedWordsUseCase: FetchInstabugCountedWordsUseCase,
    val sortCountedWordsUseCase: SortCountedWordsUseCase,
    val searchWordUseCase: SearchForWordUseCase
    ) {
}