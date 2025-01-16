package com.urwayittech.colt.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.urwayittech.colt.databinding.ActivityLoginBinding
import com.urwayittech.colt.R
import com.urwayittech.colt.api.UserViewModel
import com.urwayittech.colt.utils.SecurityClass
import com.urwayittech.colt.utils.ValidationData
import kotlinx.coroutines.launch

class LoginActivity : SecurityClass() {

    lateinit var binding: ActivityLoginBinding
    lateinit var userViewModel: UserViewModel
   

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        userViewModel=ViewModelProvider(this)[UserViewModel::class.java]
        binding.btLogin.setOnClickListener {
            if (checkValidation()) {
                login()
               }
        }

    }
    private fun checkValidation(): Boolean {
        if (binding.etEmail.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_email))
            binding.etEmail.requestFocus()
            return false
        } else if (!ValidationData.isEmail(binding.etEmail.text.toString())) {
            alertErrorDialog(getString(R.string.fill_valid_phone))
            binding.etEmail.requestFocus()
            return false
        } else if (binding.etPassword.text.isEmpty()) {
            alertErrorDialog(getString(R.string.fill_password))
            binding.etPassword.requestFocus()
            return false
        } else if (!isNetworkAvailable()) {
            alertErrorDialog(getString(R.string.no_internet))
            return false
        } else {
            return true
           }
        }

    private fun login() {
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            userViewModel.login(binding.etEmail.text.toString(),binding.etPassword.text.toString()).observe(this@LoginActivity) { jsonObject ->
                binding.tvProgressBar.visibility = View.GONE
                val jsonObjectData = checkResponse(jsonObject)

                if (jsonObjectData != null) {
                    setClientId(jsonObjectData["emp_id"].asString)
                    setClientName(jsonObjectData["name"].asString)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }

            }

        }
    }
}