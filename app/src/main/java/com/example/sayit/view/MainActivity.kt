package com.example.sayit.view

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sayit.R
import com.example.sayit.adapter.WordAdapter
import com.example.sayit.databinding.ActivityMainBinding
import com.example.sayit.repository.Result

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: WordAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvWords.layoutManager = layoutManager
        adapter = WordAdapter()
        binding.rvWords.adapter = adapter

        mainViewModel.getWordsFromApi().observe(this@MainActivity) {result ->
            when (result) {
                is Result.Success  -> {
                    adapter.submitList(result.data)
                }
                is Result.Loading -> {
                    //
                }
                is Result.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)
        return true
    }
}