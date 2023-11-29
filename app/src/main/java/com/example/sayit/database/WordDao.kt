package com.example.sayit.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sayit.model.WordItem

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(story: List<WordItem>)

    @Query("SELECT * FROM word_item")
    fun getAllWords(): LiveData<List<WordItem>>

    @Query("DELETE FROM word_item")
    suspend fun deleteAll()

    @Query("SELECT * FROM word_item WHERE word LIKE '%' || :searchQuery || '%'")
    fun searchWords(searchQuery: String): LiveData<List<WordItem>>

}