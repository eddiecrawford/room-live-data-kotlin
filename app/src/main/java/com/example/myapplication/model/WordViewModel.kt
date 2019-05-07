package com.example.myapplication.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.db.WordRepository
import com.example.myapplication.db.WordRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
Warning: A memory leak can occur if you keep in the ViewModel a reference to a context with a shorter lifecycle:

Activity
Fragment
View
All these objects can be destroyed by the operative system and recreated when there's a configuration change, and this can happen many times during the lifecycle of a ViewModel.

As an example, if you keep a reference of an Activity in the ViewModel, you may end up with a reference to a destroyed Activity. This is a memory leak.

If you need the application context, use AndroidViewModel, as shown in this codelab.
 */

class WordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WordRepository
    val allWords: LiveData<List<Word>>

    init {
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)
        allWords = repository.allWords
    }

    fun insert(word: Word) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }
}