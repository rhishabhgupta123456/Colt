package com.urwayittech.colt.api

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiRequest {

    @GET("/salesapp/index.php")
    fun loginApi(
        @Query("username") userName: String,
        @Query("password") password: String,
    ): Call<JsonObject?>?

    @Multipart
    @POST("salesapp/savedata.php")
    fun saveData(
        @Part("img_location") imgLocation: RequestBody,
        @Part("img_datetime") imgDatetime: RequestBody,
        @Part("cmpname") cmpName: RequestBody,
        @Part("cmpadd") cmpAdd: RequestBody,
        @Part("contperson") contPerson: RequestBody,
        @Part("contemail") contEmail: RequestBody,
        @Part("contmobno") contMobNo: RequestBody,
        @Part("altcontmobno") altContMobNo: RequestBody,
        @Part("contdesignation") contDesignation: RequestBody,
        @Part("cmpwebsite") cmpWebsite: RequestBody,
        @Part("cmparea") cmpArea: RequestBody,
        @Part("cmpabout") cmpAbout: RequestBody,
        @Part("anyremarks") anyRemarks: RequestBody,
        @Part customerSign: MultipartBody.Part,
        @Part("btnsubmit") btnSubmit: RequestBody,
        @Part("emp_id") empId: RequestBody
    ): Call<JsonObject?>?

}
