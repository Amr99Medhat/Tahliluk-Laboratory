package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.repositories.SettingsFragmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsFragmentViewModel : ViewModel() {
    private var mSettingsFragmentRepository: SettingsFragmentRepository =
        SettingsFragmentRepository()

    suspend fun getAppLanguage(context: Context): MutableStateFlow<String> {
        return mSettingsFragmentRepository.getAppLanguage(context)
    }

    suspend fun getAppDarkModeState(context: Context): MutableStateFlow<String> {
        return mSettingsFragmentRepository.getAppDarkModeState(context)
    }

    fun saveAppNewLanguage(context: Context, newLanguage: String) {
        viewModelScope.launch {
            mSettingsFragmentRepository.saveAppNewLanguage(context, newLanguage)
        }
    }

    fun saveAppNewDarkModeState(context: Context, newDarkModeState: String) {
        viewModelScope.launch {
            mSettingsFragmentRepository.saveAppNewDarkModeState(context, newDarkModeState)
        }
    }
}