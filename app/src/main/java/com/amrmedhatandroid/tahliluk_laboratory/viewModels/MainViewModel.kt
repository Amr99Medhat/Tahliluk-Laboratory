package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.amrmedhatandroid.tahliluk_laboratory.repositories.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {
    private val mMainRepository: MainRepository = MainRepository()

    suspend fun getToken(): MutableStateFlow<String> {
        return mMainRepository.getToken()
    }

    suspend fun updateToken(
        context: Context,
        token: String
    ): MutableStateFlow<String> {
        return mMainRepository.updateToken(context, token)
    }

}