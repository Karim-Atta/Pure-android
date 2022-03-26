package com.example.instabugandroidchallenge.countedwords.domain.interactor

import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord

class SearchForWordUseCase {

    operator fun invoke(word: String,countedWords: List<CountedWord>): List<CountedWord>{
        return countedWords.filter { countedWord ->
            (countedWord.word.contains(word))
        }
    }
}
