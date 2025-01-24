package com.urwayittech.colt.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.urwayittech.colt.databinding.ActivityMainBinding
import com.urwayittech.colt.R
import com.urwayittech.colt.api.UserViewModel
import com.urwayittech.colt.utils.MediaUtility
import com.urwayittech.colt.utils.PermissionUtils
import com.urwayittech.colt.utils.SecurityClass
import com.urwayittech.colt.utils.ValidationData
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : SecurityClass() {

    lateinit var binding: ActivityMainBinding
    lateinit var userViewModel: UserViewModel

    var LOCATION_PERMISSION_REQUEST_CODE = 100

    var latitude: Double? = null
    var longitude: Double? = null

    var filePart: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]


        binding.btSubmitForm.setOnClickListener {
            if (checkValidation()) {
                submitForm()
            }
        }

        binding.btUploadPhoto.setOnClickListener {
            browseCameraAndGallery()
        }

    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.checkAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }

                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }

            else -> {
                PermissionUtils.askAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
            } else {
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(
                this,
                "${it.localizedMessage} ?: \"Error getting location",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // Location Permission
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }

                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    PermissionUtils.showGPSNotEnabledDialog(this)
                }
            }
        }
    }


    private fun checkValidation(): Boolean {
        if (binding.etCompanyName.text.isEmpty()) {
            alertErrorDialog(getString(R.string.please_fill_the_company_name))
            return false
        } else if (binding.etCompanyAddress.text.isEmpty()) {
            alertErrorDialog("Please fill the company address!")
            return false
        } else if (binding.etContactPerson.text.isEmpty()) {
            alertErrorDialog("Please fill the contact person!")
            return false
        } else if (binding.etEmail.text.isEmpty()) {
            alertErrorDialog("Please fill the email id!")
            return false
        } else if (!ValidationData.isEmail(binding.etEmail.text.toString())) {
            alertErrorDialog("Please fill the valid email id!")
            return false
        } else if (binding.etMobileNumber.text.isEmpty()) {
            alertErrorDialog("Please fill the mobile number!")
            return false
        } else if (!ValidationData.isPhoneNumber(binding.etMobileNumber.text.toString())) {
            alertErrorDialog("Please fill the valid mobile number!")
            return false
        } else if (binding.etDesignation.text.isEmpty()) {
            alertErrorDialog("Please fill the designation!")
            return false
        } else if (binding.etCompanyArea.text.isEmpty()) {
            alertErrorDialog("Please fill the company area!")
            return false
        } else if (binding.etProductManufacture.text.isEmpty()) {
            alertErrorDialog("Please fill the product manufacture!")
            return false
        } else if (filePart == null) {
            alertErrorDialog("Please upload the photo!")
            return false
        } else {
            return true
        }
    }

    private fun browseCameraAndGallery() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose Image", "Cancel")
        val builder = AlertDialog.Builder(
            this
        )
        builder.setTitle("Choose File")
        builder.setItems(
            items
        ) { dialog: DialogInterface, item: Int ->
            if (items[item] == "Take Photo") {
                try {
                    cameraIntent()
                } catch (e: java.lang.Exception) {
                    Log.v("Exception", e.message!!)
                }
            } else if (items[item] == "Choose Image") {
                try {
                    galleryIntent()
                } catch (e: java.lang.Exception) {
                    Log.v("Exception", e.message!!)
                }
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    // this function is used for open the camera
    private fun cameraIntent() {
        ImagePicker.with(this)
            .crop(1080f, 1080f)
            .cameraOnly()
            .compress(1024)
            .maxResultSize(
                1080, 1080
            ).start()
    }

    // this function is used for open the gallery
    private fun galleryIntent() {
        ImagePicker.with(this).crop(1080f, 1080f).galleryOnly().compress(1024).maxResultSize(
            1080, 1080
        ).start()
    }

    // this function is used for to get image from camera or gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ImagePicker.REQUEST_CODE) {
            data?.let {
                val file = File(MediaUtility.getPath(this, data.data!!))
                val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), file)
                filePart =
                    MultipartBody.Part.createFormData("customer_sign", file.name, requestFile)
                binding.tvImagePath.text = file.name
            }
        }
    }

    private fun submitForm() {
        lifecycleScope.launch {
            binding.tvProgressBar.visibility = View.VISIBLE

            userViewModel.saveData(
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    getAddress()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    pickCurrentDateAndTime()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etCompanyName.text.toString()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etCompanyAddress.text.toString()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etContactPerson.text.toString()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etEmail.text.toString()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etMobileNumber.text.toString()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etAlternativeMobileNumber.text.toString()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etDesignation.text.toString()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etCompanyWebsite.text.toString()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etCompanyArea.text.toString()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etProductManufacture.text.toString()
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    binding.etRemark.text.toString()
                ),
                filePart!!,
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    "Submit"
                ),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    getClientId()
                )
            ).observe(this@MainActivity) { jsonObject ->
                binding.tvProgressBar.visibility = View.GONE
                val jsonObjectData = checkResponse(jsonObject)

                if (jsonObjectData != null) {
                    binding.etCompanyName.text.clear()
                    binding.etCompanyAddress.text.clear()
                    binding.etContactPerson.text.clear()
                    binding.etEmail.text.clear()
                    binding.etMobileNumber.text.clear()
                    binding.etAlternativeMobileNumber.text.clear()
                    binding.etCompanyWebsite.text.clear()
                    binding.etCompanyArea.text.clear()
                    binding.etProductManufacture.text.clear()
                    binding.etRemark.text.clear()
                    binding.tvImagePath.text = null

                    filePart = null
                    alertErrorDialog("Data Stored Successfully")
                }

            }

        }

    }

    private fun getAddress(): String {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocation(latitude!!, longitude!!, 1)
            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]
                val addressLine = address.getAddressLine(0) ?: "Address line not found"
                val locality = address.locality ?: "Locality not found"
                val subLocality = address.subLocality ?: "Locality not found"
                val adminArea = address.adminArea ?: "Admin area not found"
                val subAdminArea = address.subAdminArea ?: "Admin area not found"
                val postalCode = address.postalCode ?: "Postal code not found"

                // Combining all the details
                "$addressLine, $subLocality, $locality, $subAdminArea, $adminArea, $postalCode"
            } else {
                "District not found"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }

    private fun pickCurrentDateAndTime(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(calendar.time)
    }

}

