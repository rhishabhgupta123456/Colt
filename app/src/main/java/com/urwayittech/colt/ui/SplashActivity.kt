package com.urwayittech.colt.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.urwayittech.colt.databinding.ActivitySplashBinding
import com.urwayittech.colt.utils.SecurityClass

@SuppressLint("CustomSplashScreen")
class SplashActivity : SecurityClass() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            if (getClientId() == "" || getClientId().isEmpty()) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }, 3000)

    }
}