package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsFragmentRepository {
    private lateinit var mPreferenceManager: PreferenceManager
    private var mLanguage: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mDarkModeState: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)

    suspend fun getAppLanguage(context: Context): MutableStateFlow<String> {
        mLanguage = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        val state = mPreferenceManager.getString(Constants.KEY_APP_LANGUAGE)
        mLanguage.emit(state!!)
        return mLanguage
    }

    suspend fun getAppDarkModeState(context: Context): MutableStateFlow<String> {
        mDarkModeState = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        val appDarkModeState = mPreferenceManager.getString(Constants.KEY_APP_DARK_MODE_STATE)
        mDarkModeState.emit(appDarkModeState!!)
        return mDarkModeState
    }

    fun saveAppNewLanguage(context: Context, newLanguage: String) {
        mPreferenceManager = PreferenceManager(context)
        mPreferenceManager.putString(
            Constants.KEY_APP_LANGUAGE,
            newLanguage
        )
    }

    fun saveAppNewDarkModeState(context: Context, newDarkModeState: String) {
        mPreferenceManager = PreferenceManager(context)
        mPreferenceManager.putString(
            Constants.KEY_APP_DARK_MODE_STATE,
            newDarkModeState
        )
    }
}