package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.amrmedhatandroid.tahliluk_laboratory.repositories.ProfileFragmentRepository
import kotlinx.coroutines.flow.MutableStateFlow


class ProfileFragmentViewModel : ViewModel() {

    private var mProfileFragmentRepository: ProfileFragmentRepository = ProfileFragmentRepository()

    suspend fun getLabImage(context: Context): MutableStateFlow<ByteArray> {
        return mProfileFragmentRepository.getLabImage(context)
    }

    suspend fun getLabPhoneNumber(context: Context): MutableStateFlow<String> {
        return mProfileFragmentRepository.getLabPhoneNumber(context)
    }

    suspend fun signOut(context: Context): MutableStateFlow<String> {
        return mProfileFragmentRepository.signOut(context)
    }
}