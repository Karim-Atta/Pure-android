package com.example.instabugandroidchallenge.countedwords.presentation.model

import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import com.example.instabugandroidchallenge.countedwords.domain.model.ArrangingType

data class CountedWordsUIState(
    val countedWords: List<CountedWord> = emptyList(),
    var nextArrangingActionType: ArrangingType = ArrangingType.DESCENDING,
    var queryText : String? = null,
    val filteredList: List<CountedWord> = emptyList(),
    val errorMessage: String? = null,
    val empty: Boolean = false,
    val showShowProgressBar: Boolean = true)
