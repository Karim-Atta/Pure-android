package com.example.instabugandroidchallenge.countedwords.domain.model

enum class ArrangingType {
    ASCENDING, DESCENDING
}

fun ArrangingType.getNextArrangingType(): ArrangingType {
    return if (this == ArrangingType.DESCENDING){
        ArrangingType.ASCENDING
    }else
        ArrangingType.DESCENDING
}
