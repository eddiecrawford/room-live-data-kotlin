package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.Word
import com.example.myapplication.model.WordViewModel
import com.example.myapplication.view.WordListAdapter
import kotlinx.android.synthetic.main.activity_main.*

// https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#13

const val NEW_WORD_ACTIVITY_REQUEST_CODE = 1

class MainActivity : AppCompatActivity() {
    
    private lateinit var wordViewModel: WordViewModel
    private lateinit var adapter: WordListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initRecyclerView()
        initViewModel()
        initClickListenter()
    }

    private fun initViewModel() {
        wordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)
        wordViewModel.allWords.observe(this, Observer { words ->
            words?.let { adapter.setWords(it) }
        })
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        adapter = WordListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initClickListenter() {
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val word = Word(it.getStringExtra(NewWordActivity.EXTRA_REPLY))
                wordViewModel.insert(word)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }
}
