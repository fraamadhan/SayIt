package com.example.sayit.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.sayit.R
import com.example.sayit.adapter.WordAdapter
import com.example.sayit.adapter.WordSearchAdapter
import com.example.sayit.databinding.ActivityMainBinding
import com.example.sayit.model.WordItem
import com.example.sayit.view.profile.ProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: WordAdapter
    private lateinit var searchAdapter: WordSearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvWords.layoutManager = layoutManager
        adapter = WordAdapter()
        binding.rvWords.adapter = adapter

        val layoutSearchManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvSearchWord.layoutManager = layoutSearchManager
        searchAdapter = WordSearchAdapter()
        binding.rvSearchWord.adapter = searchAdapter

        showLoading()
        mainViewModel.getUserToken().observe(this) { token ->
            mainViewModel.getWordsFromApi(token.toString()).observe(this@MainActivity) {result ->
                success(result)
            }
        }

        mainViewModel.searchResults.observe(this@MainActivity) { searchResults ->
            binding.rvWords.visibility = View.GONE
            searchResults?.let {
                if (it.isEmpty()) {
                    binding.notfound.visibility = View.VISIBLE
                    binding.rvSearchWord.visibility = View.GONE
                }
                else {
                    lifecycleScope.launch(Dispatchers.Main) {
                        searchAdapter.submitList(it)
                        binding.rvSearchWord.visibility = View.VISIBLE
                        binding.notfound.visibility = View.GONE
                        Log.d("ADAPTER Size", searchAdapter.itemCount.toString())
                    }
                }
            }
        }

        setUpSearchWord()

        val onBackPressedCallback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(this@MainActivity, "Bye Owl", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.my_profile -> {
                startActivity(Intent(this, ProfileActivity:: class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun success(words: PagingData<WordItem>) {
        binding.apply {
            progressBar.visibility = View.GONE
            rvWords.visibility = View.VISIBLE
        }
        adapter.submitData(lifecycle, words)

    }
    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            rvWords.visibility = View.GONE
        }
    }

    private fun setUpSearchWord() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener {_, _, _ ->
                mainViewModel.search(searchView.text.toString())
                searchView.hide()
                false
            }
            searchBar.inflateMenu(R.menu.searchbar_menu)
            searchBar.setOnMenuItemClickListener {menuItem ->
                when(menuItem.itemId) {
                    R.id.refresh_page -> {
                        val intent = Intent(this@MainActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        true
                    }
                    else -> {
                        false
                    }

                }
            }
        }
    }

}