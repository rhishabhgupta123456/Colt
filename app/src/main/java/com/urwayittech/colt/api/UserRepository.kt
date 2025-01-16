package com.urwayittech.colt.api

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val apiInterface: ApiInterface = ApiInterface()

    fun login(
        emailOrPhone: String,
        getPassword: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.signIn(
            emailOrPhone,
            getPassword
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data
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
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        val data: SingleLiveEvent<BaseResponse<JsonObject>> = SingleLiveEvent()

        val obj: BaseResponse<JsonObject> = BaseResponse()


        apiInterface.saveData(
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
        )?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>,
                response: Response<JsonObject?>,
            ) {
                try {

                    if (response.body() != null) {
                        obj.setResponseAlt(response.body()!!)
                        obj.setIsErrorAlt(false)
                    } else {
                        obj.setMessageAlt("Server error")
                        obj.setIsErrorAlt(true)
                    }
                    data.value = obj
                } catch (e: Exception) {
                    obj.setIsErrorAlt(true)
                    obj.setMessageAlt(e.message.toString())
                    data.value = obj
                }
            }

            override fun onFailure(
                call: Call<JsonObject?>,
                t: Throwable,
            ) {
                obj.setIsErrorAlt(true)
                obj.setMessageAlt(t.message.toString())
                data.value = obj
            }

        })

        return data
    }
}