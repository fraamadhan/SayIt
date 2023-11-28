//package com.example.sayit.database
//
//import androidx.paging.PagingSource
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.sayit.model.WordItem
//
//@Dao
//interface WordDao {
//    interface StoryDao {
//        @Insert(onConflict = OnConflictStrategy.REPLACE)
//        suspend fun insertStory(story: List<WordItem>)
//
//        @Query("SELECT * FROM word_item")
//        fun getAllStory(): PagingSource<Int, WordItem>
//
//        @Query("DELETE FROM word_item")
//        suspend fun deleteAll()
//    }
//}