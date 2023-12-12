package com.example.sayit.view.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sayit.databinding.ActivitySplashScreenBinding
import com.example.sayit.view.MainActivity
import com.example.sayit.view.ViewModelFactory
import com.example.sayit.view.login.LoginActivity
import com.example.sayit.view.login.LoginViewModel

class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_SCREEN: Long = 4000;

    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel by viewModels<LoginViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(binding.root)

        keepLogin()
    }

    private fun keepLogin() {
        viewModel.getSession().observe(this@SplashScreenActivity) { user ->
            if (user.isLogin) {
                launchMainActivity()
            }
            else {
                startAnimation()
            }
        }

    }

    private fun launchMainActivity() {
        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
    }


    private fun startAnimation() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN)
    }
}