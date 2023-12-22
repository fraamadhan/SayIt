package com.example.sayit.utils

import com.example.sayit.model.WordItem
import com.example.sayit.model.WordResponse

object DataMapper {
    fun getWordsDataClassed(words: WordResponse?): List<WordItem> {
        val wordList = mutableListOf<WordItem>()

        words?.word?.map { word ->
            val wordItem = WordItem(
                id = word?.id.toString(),
                word = word?.word.toString(),
            )
            wordList.add(wordItem)
        }

        return wordList
    }
}