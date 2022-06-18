package com.amrmedhatandroid.tahliluk_laboratory.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ActivityLocationBinding
import com.amrmedhatandroid.tahliluk_laboratory.databinding.DialogProgressBinding
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import java.util.*

class LocationActivity : AppCompatActivity() {
    private lateinit var mActivityLocationBinding: ActivityLocationBinding
    private var mCurrentLatLong: LatLng? = null
    private  var mAddress:String?=null
    private var parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
    private lateinit var bindingDialog: DialogProgressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityLocationBinding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(mActivityLocationBinding.root)
        bindingDialog = DialogProgressBinding.inflate(layoutInflater)
        mCurrentLatLong = LatLng(0.0, 0.0)
        checkPermission()


        mActivityLocationBinding.btnGetLocation.setOnClickListener {
            getCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        SupportClass.showProgressBar(
            this, getString(R.string.getting_address),
            bindingDialog.tvProgressText
        )

        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@LocationActivity)
                        .removeLocationUpdates(this)
                    if (locationResult.locations.size > 0) {
                        val index = locationResult.locations.size - 1
                        val latitude = locationResult.locations[index].latitude
                        val longitude = locationResult.locations[index].longitude
                        mCurrentLatLong = LatLng(latitude, longitude)
                        Log.d("location",mCurrentLatLong!!.latitude.toString())
                        getAddress(mCurrentLatLong!!.latitude, mCurrentLatLong!!.longitude)
                    }
                }
            }, Looper.getMainLooper())
    }
    private fun getAddress(latitude:Double,longitude:Double){
        val addresses: List<Address>
        val geocoder = Geocoder(this, Locale.getDefault())
        addresses = geocoder.getFromLocation(latitude , longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        val address: String = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        mAddress = address
        val intent = Intent()
        intent.putExtra(Constants.KEY_LOCATION_RESULT, mCurrentLatLong)
        intent.putExtra(Constants.KEY_LOCATION_Address_Result,mAddress)
        setResult(Constants.KEY_LOCATION_RESULT_CODE, intent)
        onBackPressed()



    }

    private fun checkPermission(){
        if ( ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),101)
        }

    }
}