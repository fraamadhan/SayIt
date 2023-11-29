package com.example.sayit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_item")
data class WordItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String,
)
