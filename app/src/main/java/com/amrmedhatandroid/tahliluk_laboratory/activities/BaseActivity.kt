package com.amrmedhatandroid.tahliluk_laboratory.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.BaseViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class BaseActivity : AppCompatActivity() {
    private lateinit var mBaseViewModel: BaseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBaseViewModel = ViewModelProvider(this)[BaseViewModel::class.java]

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            mBaseViewModel.setAvailabilityOnline(applicationContext).collect {
                when (it) {
                    Constants.KEY_FALSE_RETURN -> {
                        Log.d("here-setAvailabilityOnline", "Set Availability Online Failed")
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            mBaseViewModel.setAvailabilityOffline(applicationContext).collect {
                when (it) {
                    Constants.KEY_FALSE_RETURN -> {
                        Log.d("here-setAvailabilityOffline", "Set Availability Offline Failed")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mBaseViewModel.viewModelScope.cancel()
    }
}