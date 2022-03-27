package com.example.instabugandroidchallenge.countedwords.presentation.view

import android.app.SearchManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instabugandroidchallenge.R
import com.example.instabugandroidchallenge.core.AppContainer
import com.example.instabugandroidchallenge.core.ChallengeApplication
import com.example.instabugandroidchallenge.core.EspressoIdlingResource
import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import com.example.instabugandroidchallenge.countedwords.domain.model.ArrangingType
import com.example.instabugandroidchallenge.countedwords.presentation.di.CountedWordsContainer
import com.example.instabugandroidchallenge.countedwords.presentation.view.adapter.CountedWordsAdapter
import com.example.instabugandroidchallenge.countedwords.presentation.viewmodel.CountedWordViewModel
import com.example.instabugandroidchallenge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appContainer: AppContainer
    private lateinit var countedWordViewModel: CountedWordViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAppContainer()
        instantiateCountedWordsViewModel()
        setupCountedWordsList()
        countedWordViewModel.state.value?.let {currentState ->
            if (currentState.countedWords.isEmpty()) {
                requestCountedWords()
            }
        }
        observeUIState()
    }

    private fun observeUIState() {
        countedWordViewModel.state.observe(this, { countedWordsState ->
            EspressoIdlingResource.decrement()
            setViewToDefault()
            when {
                countedWordsState.showShowProgressBar -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                countedWordsState.errorMessage != null -> {
                    binding.tvError.text = countedWordsState.errorMessage
                }
                countedWordsState.filteredList.isNotEmpty() -> {
                    bindCountedWordList(countedWordsState.filteredList)
                }
                countedWordsState.empty -> {
                    binding.tvError.text = getString(R.string.empty_error)
                }
                countedWordsState.countedWords.isNotEmpty() -> {
                    bindCountedWordList(countedWordsState.countedWords)
                }
            }
        })
    }

    private fun setViewToDefault() {
        bindCountedWordList(emptyList())
        invalidateOptionsMenu()
        binding.progressBar.visibility = View.INVISIBLE
        binding.tvError.text = ""
    }

    private fun bindCountedWordList(countedWords: List<CountedWord>) {
        binding.countedWordsList.adapter?.let { listAdapter ->
            (listAdapter as CountedWordsAdapter)
                .also { countedWordsAdapter ->
                    countedWordsAdapter.data = countedWords
                    countedWordsAdapter.notifyDataSetChanged()
                }
        }
    }

    private fun requestCountedWords() {
        EspressoIdlingResource.increment()
        appContainer.countedWordsContainer?.let { countedWordsContainer ->
            countedWordsContainer.connectivityManager.also { connectivityManager ->
                val isNetworkConnected = connectivityManager.activeNetwork != null
                countedWordViewModel.fetchCountedWords(isNetworkConnected)
            }
        }
    }

    private fun setupCountedWordsList() {
        binding.countedWordsList.layoutManager = LinearLayoutManager(this)
        binding.countedWordsList.adapter = CountedWordsAdapter()
        binding.countedWordsList.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                LinearLayoutManager.VERTICAL,
            )
        )
    }

    private fun setupAppContainer() {
        appContainer = ChallengeApplication().appContainer
        appContainer.countedWordsContainer = CountedWordsContainer(this)
    }

    private fun instantiateCountedWordsViewModel() {
        val countedWordViewModelFactory =
            appContainer.countedWordsContainer?.countedWordsViewModelFactory

       countedWordViewModelFactory?.let { countedViewModelFactory ->
            countedWordViewModel = ViewModelProvider(
                this,
                countedViewModelFactory
            ).get(CountedWordViewModel::class.java)
           countedWordViewModel
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.action_search)?.let { searchItem ->
            handleSearchItem(searchItem)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun handleSearchItem(searchItem: MenuItem) {
        (searchItem.actionView as SearchView).also { searchView ->
            setQueryTextListener(searchView)
            countedWordViewModel.currentUIState.queryText?.let { queryText ->
                handleSearchViewState(searchView, searchItem, queryText)
            }
            (getSystemService(SEARCH_SERVICE) as SearchManager).also { searchManager ->
                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            }
        }
    }

    private fun handleSearchViewState(
        searchView: SearchView,
        searchItem: MenuItem,
        queryText: String
    ){
        searchView.isIconified = false
        searchItem.expandActionView()
        searchView.setQuery(queryText, false)
    }

    private fun setQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countedWordViewModel.currentUIState.also { countedWordsUIState ->
                    if (!newText.equals(countedWordsUIState.queryText)) {
                        if (newText.isNullOrBlank()) {
                            if (countedWordsUIState.queryText != null) {
                                newText?.let {
                                    EspressoIdlingResource.increment()
                                    countedWordViewModel.search(newText)
                                }
                            }
                        }else
                            newText.let {
                                EspressoIdlingResource.increment()
                                countedWordViewModel.search(newText)
                            }
                    }
                }
                return true
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        countedWordViewModel.state.value?.let { currentUIState ->
            currentUIState.nextArrangingActionType.also { arrangingType ->
                if (arrangingType == ArrangingType.DESCENDING) {
                    menu.findItem(R.id.action_sort).icon = AppCompatResources.getDrawable(
                        this,
                        R.drawable.descending_order
                    )
                } else {
                    menu.findItem(R.id.action_sort).icon = AppCompatResources.getDrawable(
                        this,
                        R.drawable.ascending_order
                    )
                }
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort -> {
                EspressoIdlingResource.increment()
                countedWordViewModel.sortCountedOrder()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        appContainer.countedWordsContainer = null
        super.onDestroy()
    }
}