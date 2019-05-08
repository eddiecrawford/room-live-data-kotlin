package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.model.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): WordRoomDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordRoomDatabase::class.java,
                        "word_database"
                )
                .addCallback(WordDatabaseCallback(scope))
                .build()

                INSTANCE = instance
                return instance
            }
        }
    }

    private class WordDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    initializeFirstData(database.wordDao())
                }
            }
        }

        fun initializeFirstData(wordDao: WordDao) {
            // wordDao.deleteAll()
            var word = Word("Placeholder")
            wordDao.insertWord(word)
        }
    }
}

