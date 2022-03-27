package com.example.instabugandroidchallenge.countedwords.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instabugandroidchallenge.core.EspressoIdlingResource
import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import com.example.instabugandroidchallenge.countedwords.domain.interactor.CountedWordsUseCases
import com.example.instabugandroidchallenge.countedwords.domain.model.ArrangingType
import com.example.instabugandroidchallenge.countedwords.domain.model.getNextArrangingType
import com.example.instabugandroidchallenge.countedwords.presentation.model.CountedWordsUIState

class CountedWordViewModel(private val countedWordsUseCases: CountedWordsUseCases): ViewModel() {
    private val _state = MutableLiveData(CountedWordsUIState())
    val state: LiveData<CountedWordsUIState> = _state

    val currentUIState: CountedWordsUIState
        get() = state.value as CountedWordsUIState

    fun fetchCountedWords(isNetworkConnected: Boolean) {
        countedWordsUseCases.fetchCountedWordsUseCase(isNetworkConnected)
            .also { countedWordsFuture ->
                countedWordsFuture.thenAccept { countedWords ->
                    _state.postValue(
                        currentUIState.copy(
                            countedWords = countedWords,
                            filteredList = emptyList(),
                            errorMessage = null,
                            empty = countedWords.isEmpty(),
                            showShowProgressBar = false
                        )
                    )
                }.exceptionally { throwable ->
                    throwable.localizedMessage?.let { localizedMessage ->
                        _state.postValue(
                            currentUIState.copy(
                                countedWords = emptyList(),
                                filteredList = emptyList(),
                                errorMessage = localizedMessage,
                                empty = false,
                                showShowProgressBar = false
                            )
                        )
                    }
                    null
                }
            }
    }

    fun sortCountedOrder() {
        currentUIState.also { currentUIState ->
            countedWordsUseCases.sortCountedWordsUseCase(
                currentUIState.countedWords,
                currentUIState.nextArrangingActionType
            ).also { sortedCountedWords ->
                countedWordsUseCases.sortCountedWordsUseCase(
                    currentUIState.filteredList,
                    currentUIState.nextArrangingActionType
                ).also { sortedFilteredCountedWords ->
                    _state.postValue(
                        this.currentUIState.copy(
                            countedWords = sortedCountedWords,
                            filteredList = sortedFilteredCountedWords,
                            errorMessage = null,
                            empty = false,
                            nextArrangingActionType = currentUIState.nextArrangingActionType.getNextArrangingType(),
                            showShowProgressBar = false
                        )
                    )
                }
            }
        }
    }

    fun search(word: String) {
        currentUIState.also { currentUIState ->
            countedWordsUseCases.searchWordUseCase(
                word,
                currentUIState.countedWords
            ).also { filteredCountedWords ->
                _state.postValue(
                    currentUIState.copy(
                        countedWords = currentUIState.countedWords,
                        filteredList = filteredCountedWords,
                        errorMessage = null,
                        empty = filteredCountedWords.isEmpty(),
                        queryText = word,
                        showShowProgressBar = false
                    )
                )
            }
        }
    }
}