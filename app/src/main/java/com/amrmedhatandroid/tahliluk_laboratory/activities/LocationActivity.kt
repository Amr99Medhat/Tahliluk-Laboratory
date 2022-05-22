package com.amrmedhatandroid.tahliluk_laboratory.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ActivityLocationBinding
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.google.android.gms.maps.model.LatLng

class LocationActivity : AppCompatActivity() {
    private lateinit var mActivityLocationBinding: ActivityLocationBinding
    private var mCurrentLatLong: LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityLocationBinding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(mActivityLocationBinding.root)

        mCurrentLatLong = LatLng(0.0, 0.0)

        mActivityLocationBinding.button.setOnClickListener {
            val intent = Intent()
            intent.putExtra(Constants.KEY_LOCATION_RESULT, mCurrentLatLong)
            setResult(Constants.KEY_LOCATION_RESULT_CODE, intent)

            onBackPressed()
        }
    }
}