package com.example.sayit.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sayit.R
import com.example.sayit.adapter.WordAdapter
import com.example.sayit.databinding.ActivityMainBinding
import com.example.sayit.view.profile.ProfileActivity

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

//        showLoading()
//        mainViewModel.getWordsFromApi().observe(this@MainActivity) {result ->
//            when (result) {
//                is Result.Success  -> {
//                    showSuccess()
//                    adapter.setData(result.data)
////                    mainViewModel.deleteALl()
//                }
//                is Result.Loading -> {
//                    showLoading()
//                }
//                is Result.Error -> {
//                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

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
        val menuItem = menu!!.findItem(R.id.search_icon)
        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

        })

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


    private fun showSuccess() {
        binding.apply {
            progressBar.visibility = View.GONE
            rvWords.visibility = View.VISIBLE
        }
    }
    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            rvWords.visibility = View.GONE
        }
    }

}