package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import com.amrmedhatandroid.tahliluk_laboratory.repositories.SplashScreenRepository
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("CustomSplashScreen")
class SplashScreenViewModel : ViewModel() {
    private var mSplashScreenRepository: SplashScreenRepository = SplashScreenRepository()

    suspend fun getSignInState(context: Context): MutableStateFlow<Boolean> {
        return mSplashScreenRepository.getSignInState(context)
    }

    suspend fun getAppDarkModeState(context: Context): MutableStateFlow<String> {
        return mSplashScreenRepository.getAppDarkModeState(context)
    }

    suspend fun getAppLanguage(context: Context): MutableStateFlow<String> {
        return mSplashScreenRepository.getAppLanguage(context)
    }
}