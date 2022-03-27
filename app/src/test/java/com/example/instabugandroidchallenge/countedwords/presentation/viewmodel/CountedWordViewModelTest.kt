package com.example.instabugandroidchallenge.countedwords.presentation.viewmodel

import com.example.instabugandroidchallenge.countedwords.getOrAwaitValueTest
import com.example.instabugandroidchallenge.countedwords.presentation.di.FakeCountedWordsContainer
import org.junit.Before
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

class CountedWordViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CountedWordViewModel

    @Before
    fun setup() {
        viewModel = CountedWordViewModel(FakeCountedWordsContainer().countedWordUseCases)
    }

    @Test
    fun `fetch counted with internet`() {
        viewModel.fetchCountedWords(true)
        val data = viewModel.state.getOrAwaitValueTest().countedWords
        assert(data[0].word == "second")
    }

    @Test
    fun `fetch counted array with no Internet should return stored array`(){
        viewModel.fetchCountedWords(false)
        val data = viewModel.state.getOrAwaitValueTest().countedWords
        assert(data[0].word == "stored second")
    }
}