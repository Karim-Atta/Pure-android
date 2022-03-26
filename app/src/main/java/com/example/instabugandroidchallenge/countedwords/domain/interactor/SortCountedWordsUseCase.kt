package com.example.instabugandroidchallenge.countedwords.domain.interactor

import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import com.example.instabugandroidchallenge.countedwords.domain.model.ArrangingType
import com.example.instabugandroidchallenge.countedwords.domain.sortedByRepeatedCount

class SortCountedWordsUseCase {

    operator fun invoke(countedWords: List<CountedWord>, arrangingType: ArrangingType): List<CountedWord> {
        return countedWords.sortedByRepeatedCount(arrangingType)
    }
}
