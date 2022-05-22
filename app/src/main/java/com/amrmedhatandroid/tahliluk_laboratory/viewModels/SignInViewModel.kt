package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.models.Lab
import com.amrmedhatandroid.tahliluk_laboratory.repositories.SignInRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private val mSignInRepository: SignInRepository = SignInRepository()

    suspend fun signIn(
        inputNumber: String,
        inputPassword: String
    ): MutableStateFlow<Lab> {
        onCleared()
        return mSignInRepository.signIn(
            inputNumber,
            inputPassword
        )
    }

    fun saveBasicData(
        context: Context,
        lab: Lab
    ) {
        viewModelScope.launch {
            mSignInRepository.saveBasicData(
                context,
                lab
            )
        }
    }
}