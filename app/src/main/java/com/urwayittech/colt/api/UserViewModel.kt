package com.urwayittech.colt.api

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserViewModel : ViewModel() {

    private val userRepository: UserRepository = UserRepository()

    fun login(
        emailOrPhone: String,
        getPassword: String,
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return userRepository.login(emailOrPhone, getPassword)
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
        empId : RequestBody
    ): SingleLiveEvent<BaseResponse<JsonObject>> {
        return userRepository.saveData(
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