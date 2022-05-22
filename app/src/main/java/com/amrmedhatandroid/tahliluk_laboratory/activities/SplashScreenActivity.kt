package com.amrmedhatandroid.tahliluk_laboratory.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ActivitySplashScreenBinding
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.SplashScreenViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var mActivitySplashScreenBinding: ActivitySplashScreenBinding
    private lateinit var mSplashScreenViewModel: SplashScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivitySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(mActivitySplashScreenBinding.root)
        mSplashScreenViewModel = ViewModelProvider(this)[SplashScreenViewModel::class.java]

        lifecycleScope.launchWhenResumed {
            setDarkModeState()
        }

        lifecycleScope.launchWhenResumed {
            setAppLanguage()
        }

        lifecycleScope.launchWhenResumed {
            setAnimation()
        }

        lifecycleScope.launchWhenResumed {
            delay(3800)
            moveToSignIn()
        }

    }

    private suspend fun setDarkModeState() {
        mSplashScreenViewModel.getAppDarkModeState(this@SplashScreenActivity).collect { state ->
            when (state) {
                Constants.KEY_DARK_MODE -> {
                    SupportClass.startNightMode()
                }
                Constants.KEY_LIGHT_MODE -> {
                    SupportClass.startLightMode()
                }
                else -> {
                    SupportClass.startLightMode()
                }
            }
        }
    }

    private suspend fun setAppLanguage() {
        mSplashScreenViewModel.getAppLanguage(this@SplashScreenActivity).collect { language ->
            when (language) {
                Constants.KEY_EMPTY -> {
                    SupportClass.setLocale(Constants.KEY_LANGUAGE_ENGLISH, resources)
                }
                Constants.KEY_LANGUAGE_ENGLISH -> {
                    SupportClass.setLocale(Constants.KEY_LANGUAGE_ENGLISH, resources)
                }
                Constants.KEY_LANGUAGE_ARABIC -> {
                    SupportClass.setLocale(Constants.KEY_LANGUAGE_ARABIC, resources)
                }
            }
        }
    }

    private fun setAnimation() {
        val appName: Animation = AnimationUtils.loadAnimation(this, R.anim.app_name)
        val description: Animation = AnimationUtils.loadAnimation(this, R.anim.description)
        mActivitySplashScreenBinding.tvDescription.startAnimation(description)
        mActivitySplashScreenBinding.tvAppName.startAnimation(appName)
    }

    private suspend fun moveToSignIn() {
        mSplashScreenViewModel.getSignInState(application).collect { state ->
            if (state) {
                SupportClass.startActivityWithFlag(this, MainActivity::class.java)

            } else {
                SupportClass.startActivityWithFlag(this, SignInActivity::class.java)
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mSplashScreenViewModel.viewModelScope.cancel()
    }
}