package com.example.sayit.view.detailword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sayit.databinding.ActivityDetailWordBinding

class DetailWordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailWordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailWordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}