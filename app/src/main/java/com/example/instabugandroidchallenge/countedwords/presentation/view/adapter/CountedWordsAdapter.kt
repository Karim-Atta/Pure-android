package com.example.instabugandroidchallenge.countedwords.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instabugandroidchallenge.countedwords.data.source.model.CountedWord
import com.example.instabugandroidchallenge.databinding.CountedWordItemLayoutBinding

class CountedWordsAdapter(var data: List<CountedWord> = emptyList())
    : RecyclerView.Adapter<CountedWordsAdapter.CountedWordsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountedWordsViewHolder {
        return CountedWordItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let {countedWordsItemBinding ->
            CountedWordsViewHolder(countedWordsItemBinding)
        }
    }

    override fun onBindViewHolder(holder: CountedWordsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class CountedWordsViewHolder(private val countedWordItemBinding: CountedWordItemLayoutBinding)
        : RecyclerView.ViewHolder(countedWordItemBinding.root){
        fun bind(countedWord: CountedWord) {
            countedWordItemBinding.tvWord.text = countedWord.word
            countedWordItemBinding.tvCount.text = countedWord.count.toString()
        }
    }
}