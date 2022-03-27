package com.example.instabugandroidchallenge.countedwords.presentation.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.instabugandroidchallenge.countedwords.data.repository.FetchingCountedWordsRepository
import com.example.instabugandroidchallenge.countedwords.data.source.local.StoredCountedWordSource
import com.example.instabugandroidchallenge.countedwords.data.source.local.helper.InstabugSQLHelper
import com.example.instabugandroidchallenge.countedwords.data.source.remote.FetchingInstabugPageDataSource
import com.example.instabugandroidchallenge.countedwords.domain.interactor.*
import com.example.instabugandroidchallenge.countedwords.domain.repository.FetchingCountedWordsRepo
import com.example.instabugandroidchallenge.countedwords.presentation.viewmodel.CountedViewModelFactory
import java.util.concurrent.Executors

class CountedWordsContainer(context: Context) {

    val fetchingInstabugPageDataSource = FetchingInstabugPageDataSource()
    val instabugSQLHelper = InstabugSQLHelper(context)
    val storedCountedWordSource = StoredCountedWordSource(instabugSQLHelper.writableDatabase, instabugSQLHelper.readableDatabase)
    val fetchCountedWordsRepository : FetchingCountedWordsRepo = FetchingCountedWordsRepository(Executors.newFixedThreadPool(4), storedCountedWordSource, fetchingInstabugPageDataSource)
    private val fetchingInstabugCountedWordsUseCase = FetchInstabugCountedWordsUseCase(fetchCountedWordsRepository)
    val searchForWordUseCase = SearchForWordUseCase()
    val sortCountedUseCase = SortCountedWordsUseCase()
    val countedWordUseCases = CountedWordsUseCases(
        fetchingInstabugCountedWordsUseCase,
        sortCountedUseCase,
        searchForWordUseCase
    )
    val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    val countedWordsViewModelFactory = CountedViewModelFactory(countedWordUseCases)
}