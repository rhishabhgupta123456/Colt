package com.urwayittech.colt.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class ApiInterface {

    private var apiRequest: ApiRequest

    init {

        val headerInterceptor = Interceptor { chain ->
            val originalRequest: Request = chain.request()
            val requestWithHeaders: Request =
                originalRequest.newBuilder().addHeader("Accept", "application/json").build()
            chain.proceed(requestWithHeaders)
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }


        val okHttpClient = OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS).writeTimeout(40, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true).addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor).build()

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder().baseUrl("https://micdelhi.in/").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        apiRequest = retrofit.create(ApiRequest::class.java)


    }

    fun signIn(emailOrPhone: String, password: String): Call<JsonObject?>? {
        return apiRequest.loginApi(emailOrPhone, password)
    }


    fun saveData(
        imgLocation: RequestBody,
        imgDatetime: RequestBody,
        cmpName: RequestBody,
        cmpAdd: RequestBody,
        contPerson: RequestBody,
        contEmail: RequestBody,
        contMobNo: RequestBody,
        altContMobNo: RequestBody,
        contDesignation: RequestBody,
        cmpWebsite: RequestBody,
        cmpArea: RequestBody,
        cmpAbout: RequestBody,
        anyRemarks: RequestBody,
        customerSign: MultipartBody.Part,
        btnSubmit: RequestBody,
        empId: RequestBody
    ): Call<JsonObject?>? {
        return apiRequest.saveData(
            imgLocation,
            imgDatetime,
            cmpName,
            cmpAdd,
            contPerson,
            contEmail,
            contMobNo,
            altContMobNo,
            contDesignation,
            cmpWebsite,
            cmpArea,
            cmpAbout,
            anyRemarks,
            customerSign,
            btnSubmit,
            empId
        )
    }


}

