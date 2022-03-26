package com.example.instabugandroidchallenge.countedwords.data.repository

import androidx.core.text.HtmlCompat
import com.example.instabugandroidchallenge.countedwords.data.maping.toCountedWords
import com.example.instabugandroidchallenge.countedwords.data.source.local.StoredCountedWordSource
import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import com.example.instabugandroidchallenge.countedwords.data.source.remote.FetchingInstabugPageDataSource
import com.example.instabugandroidchallenge.countedwords.domain.repository.FetchingCountedWordsRepo
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

class FetchingCountedWordsRepository(
    private val executors: ExecutorService,
    private val storedCountedWordSource: StoredCountedWordSource,
    private val instabugPageDataSource: FetchingInstabugPageDataSource,
    ): FetchingCountedWordsRepo {
    override fun getCountedWords(isInternetConnected: Boolean): CompletableFuture<List<CountedWord>> {
        return if (isInternetConnected) {
            fetchCountedWord("https://instabug.com")
                .thenApply { countedWords ->
                    refreshLocalStorageAndGetUpdatedList(countedWords)
                }
        } else
            getStoredCountedWords()
    }

    private fun getStoredCountedWords(): CompletableFuture<List<CountedWord>> {
        return CompletableFuture.supplyAsync ({
            storedCountedWordSource.getCountedWords()
        }, executors)
    }

    private fun fetchCountedWord(websiteAddress: String):CompletableFuture<List<CountedWord>> {
        return CompletableFuture.supplyAsync ({
            val instabugSite = instabugPageDataSource.fetchInstabugPage(websiteAddress)
            val instabugWords = getWordsWithTheirRepeatedCount(instabugSite)
            val wordsToCount = getMapOfWordToItsCount(instabugWords)
            wordsToCount.toCountedWords()
        }, executors)
    }

    private fun refreshLocalStorageAndGetUpdatedList(countedWords: List<CountedWord>): List<CountedWord> {

        storedCountedWordSource.deleteCountedWords()
        countedWords.forEach { countedWord ->
            storedCountedWordSource.insertCountedWord(countedWord)
        }
        return storedCountedWordSource.getCountedWords()
    }

    private fun getMapOfWordToItsCount(instabugWords: List<String>): HashMap<String, Int>{
        val mappedWordToItsCount = HashMap<String, Int>()
        instabugWords.forEach { word ->
            if (mappedWordToItsCount.containsKey(word))
                mappedWordToItsCount[word] = (mappedWordToItsCount[word]  ?: 0) + 1
            else
                mappedWordToItsCount[word] = 1
        }
        return mappedWordToItsCount
    }

    private fun getWordsWithTheirRepeatedCount(instabugSite: String): List<String> {
        var content = (HtmlCompat.fromHtml(instabugSite, HtmlCompat.FROM_HTML_MODE_LEGACY).toString())
        content = content.replace("(?m)^@media.*".toRegex(), "")
        content = content.substring(content.indexOf("\n"))
        content = content.replace("[^A-Za-z0-9 ]".toRegex(),"")
        return content.split(" ")
    }
}