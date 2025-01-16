package com.urwayittech.colt.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.urwayittech.colt.R
import com.urwayittech.colt.ui.LoginActivity
import com.urwayittech.colt.api.BaseResponse
import java.util.Locale

open class SecurityClass() : AppCompatActivity() {


    fun setClientId(clientId: String) {
        var prefs: SharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("employeeID", clientId)
        editor.apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun getClientId(): String {
        var prefs: SharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = prefs.edit()
        return prefs.getString("employeeID", "").toString()
    }


    fun setClientName(clientId: String) {
        var prefs: SharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("employeeName", clientId)
        editor.apply()
    }

    fun getClientName(): String {
        var prefs: SharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = prefs.edit()
        return prefs.getString("employeeName", "").toString()
    }


    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun checkResponse(jsonObject: BaseResponse<JsonObject>): JsonObject? {
        if (!jsonObject.isIsError) {
            if (jsonObject.response != null) {
                try {
                    val jsonObjectData: JsonObject = jsonObject.response!!
                    val status = jsonObjectData["status"].asString

                    if (status == "OK") {
                        return jsonObjectData
                    } else {
                        alertErrorDialog(jsonObjectData["msg"].asString)
                    }
                } catch (e: Exception) {
                    alertErrorDialog("${e.message}")
                }
            }
        } else {
            logOutAccount()
            Toast.makeText(this, "Login session expired", Toast.LENGTH_SHORT).show()
        }
        return null
    }

    fun logOutAccount() {
        var prefs: SharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }


    fun alertErrorDialog(msg: String) {
        val alertErrorDialog = Dialog(this)
        alertErrorDialog.setCancelable(false)
        alertErrorDialog.setContentView(R.layout.alertbox_error)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertErrorDialog.window!!.attributes)

        Log.e("Error Message", msg)
        alertErrorDialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        alertErrorDialog.window!!.attributes = layoutParams

        val tvTitle: TextView = alertErrorDialog.findViewById(R.id.tv_title)
        val btnOk: TextView = alertErrorDialog.findViewById(R.id.btn_ok)
        tvTitle.text = msg.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

        btnOk.setOnClickListener {
            alertErrorDialog.dismiss()
        }

        alertErrorDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertErrorDialog.show()
    }
}