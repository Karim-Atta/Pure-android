package com.example.instabugandroidchallenge.countedwords.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instabugandroidchallenge.countedwords.domain.interactor.CountedWordsUseCases
import java.lang.IllegalArgumentException

class CountedViewModelFactory constructor(private val countedWordUseCases: CountedWordsUseCases):
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountedWordViewModel::class.java)) {
            return CountedWordViewModel(countedWordUseCases) as T
        } else {
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}