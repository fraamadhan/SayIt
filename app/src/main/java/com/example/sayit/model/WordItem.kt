package com.example.sayit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_item")
data class WordItem(
    @PrimaryKey
    val id: String,
    val word: String,
    val description: String? = null
)
