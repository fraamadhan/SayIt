//package com.example.sayit.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.sayit.model.WordItem
//
//@Database(
//    entities = [WordItem::class, RemoteKeys::class],
//    version = 1,
//    exportSchema = false,
//)
//abstract class WordDatabase: RoomDatabase() {
//    abstract fun wordDao(): WordDao
//    abstract fun remoteKeysDao(): RemoteKeysDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: WordDatabase? = null
//
//        @JvmStatic
//        fun getDatabase(context: Context): WordDatabase {
//            return INSTANCE ?: synchronized(this) {
//                INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    WordDatabase::class.java, "word_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                    .also { INSTANCE = it }
//            }
//        }
//    }
//}