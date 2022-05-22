package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("CustomSplashScreen")
class SplashScreenRepository {
    private lateinit var mPreferenceManager: PreferenceManager
    private var mSignInState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var mLanguage: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mDarkModeState: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)

    suspend fun getSignInState(context: Context): MutableStateFlow<Boolean> {
        mSignInState = MutableStateFlow(false)
        mPreferenceManager = PreferenceManager(context)
        val state = mPreferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)
        if (state) {
            this.mSignInState.emit(true)
        } else {
            this.mSignInState.emit(false)
        }
        return mSignInState
    }

    suspend fun getAppLanguage(context: Context): MutableStateFlow<String> {
        mLanguage = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        val appLanguage = mPreferenceManager.getString(Constants.KEY_APP_LANGUAGE)
        if (appLanguage != null) {
            this.mLanguage.emit(appLanguage)
        }
        return this.mLanguage
    }

    suspend fun getAppDarkModeState(context: Context): MutableStateFlow<String> {
        mDarkModeState = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        val appDarkModeState = mPreferenceManager.getString(Constants.KEY_APP_DARK_MODE_STATE)
        if (appDarkModeState != null) {
            Log.d("here",appDarkModeState)
            mDarkModeState.emit(appDarkModeState)
        }
        return mDarkModeState
    }
}