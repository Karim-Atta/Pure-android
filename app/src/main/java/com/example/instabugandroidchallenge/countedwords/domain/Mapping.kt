package com.example.instabugandroidchallenge.countedwords.domain

import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import com.example.instabugandroidchallenge.countedwords.domain.model.ArrangingType

fun List<CountedWord>.sortedByRepeatedCount(arrangingType: ArrangingType): List<CountedWord>{
    return when (arrangingType) {
        ArrangingType.ASCENDING -> {
            this.sortedBy { countedWord ->
                countedWord.count
            }
        }
        ArrangingType.DESCENDING -> {
            this.sortedByDescending {countedWord ->
                countedWord.count
            }
        }
    }
}