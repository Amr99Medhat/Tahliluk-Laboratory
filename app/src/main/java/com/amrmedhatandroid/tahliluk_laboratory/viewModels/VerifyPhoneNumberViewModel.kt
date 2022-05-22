package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.activities.VerifyPhoneNumberActivity
import com.amrmedhatandroid.tahliluk_laboratory.repositories.VerifyPhoneNumberRepository
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class VerifyPhoneNumberViewModel : ViewModel() {
    private var mVerifyPhoneNumberRepository: VerifyPhoneNumberRepository =
        VerifyPhoneNumberRepository()

    fun sendVerificationCodeToLab(
        activity: VerifyPhoneNumberActivity,
        phoneNo: String,
        mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        viewModelScope.launch {
            mVerifyPhoneNumberRepository.sendVerificationCodeToLab(activity, phoneNo, mCallbacks)
        }
    }

    suspend fun signInByCredentials(
        activity: VerifyPhoneNumberActivity,
        credential: PhoneAuthCredential
    ): MutableStateFlow<String> {
        return mVerifyPhoneNumberRepository.signInByCredentials(activity, credential)
    }

    suspend fun signUp(lab: HashMap<Any, Any>): MutableStateFlow<HashMap<String, String>> {
        return mVerifyPhoneNumberRepository.signUp(lab)
    }
}