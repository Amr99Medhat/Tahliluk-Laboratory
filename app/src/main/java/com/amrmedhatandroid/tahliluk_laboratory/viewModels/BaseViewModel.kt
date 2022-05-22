package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.amrmedhatandroid.tahliluk_laboratory.repositories.BaseRepository
import kotlinx.coroutines.flow.MutableStateFlow

class BaseViewModel : ViewModel() {
    private val mBaseRepository: BaseRepository = BaseRepository()

    suspend fun setAvailabilityOnline(context: Context): MutableStateFlow<String> {
        return mBaseRepository.setAvailabilityOnline(context)
    }

    suspend fun setAvailabilityOffline(context: Context): MutableStateFlow<String> {
        return mBaseRepository.setAvailabilityOffline(context)
    }
}