package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.amrmedhatandroid.tahliluk_laboratory.repositories.HomeFragmentRepository
import kotlinx.coroutines.flow.MutableStateFlow

class HomeFragmentViewModel : ViewModel() {
    private var mHomeFragmentRepository: HomeFragmentRepository = HomeFragmentRepository()

    suspend fun getLabName(context: Context): MutableStateFlow<String> {
        return mHomeFragmentRepository.getLabName(context)
    }

    suspend fun getLabImage(context: Context): MutableStateFlow<ByteArray> {
        return mHomeFragmentRepository.getLabImage(context)
    }
}