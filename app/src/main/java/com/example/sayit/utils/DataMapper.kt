package com.example.sayit.utils

import com.example.sayit.model.WordItem
import com.example.sayit.model.WordResponseItem

object DataMapper {
    fun getWordsDataClassed(words: List<WordResponseItem>?): List<WordItem> {
        val wordList = mutableListOf<WordItem>()

        words?.map { word ->
            val wordItem = WordItem(
                word = word.word.toString()
            )
            wordList.add(wordItem)
        }

        return wordList
    }
}