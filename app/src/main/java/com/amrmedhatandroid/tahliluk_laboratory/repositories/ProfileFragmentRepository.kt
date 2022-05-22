package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import android.util.Base64
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.firebase.FirestoreClass
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

class ProfileFragmentRepository {
    private lateinit var mPreferenceManager: PreferenceManager
    private var mLabImageBytes: MutableStateFlow<ByteArray> =
        MutableStateFlow(byteArrayOf(0x2E, 0x38))
    private var mLabPhoneNumber: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mSignOutState: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)

    suspend fun getLabImage(context: Context): MutableStateFlow<ByteArray> {
        mLabImageBytes = MutableStateFlow(byteArrayOf(0x2E, 0x38))
        mPreferenceManager = PreferenceManager(context)
        val bytes: ByteArray =
            Base64.decode(mPreferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT)
        mLabImageBytes.emit(bytes)
        return mLabImageBytes
    }

    suspend fun getLabPhoneNumber(context: Context): MutableStateFlow<String> {
        mLabPhoneNumber = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        val name = mPreferenceManager.getString(Constants.KEY_LAB_PHONE_NUMBER)
        if (name != null) {
            mLabPhoneNumber.emit(name)
        }
        return mLabPhoneNumber
    }

    suspend fun signOut(
        context: Context
    ): MutableStateFlow<String> {
        mSignOutState = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        FirestoreClass().signOut(
            Constants.KEY_COLLECTION_LABS,
            mPreferenceManager.getString(Constants.KEY_LAB_ID)!!,
            Constants.KEY_FCM_TOKEN
        )
            .addOnSuccessListener {
                val deviceLanguage = mPreferenceManager.getString(Constants.KEY_APP_LANGUAGE)
                val darkModeState = mPreferenceManager.getString(Constants.KEY_APP_DARK_MODE_STATE)
                mPreferenceManager.clear()
                if (deviceLanguage == Constants.KEY_LANGUAGE_ENGLISH) {
                    mPreferenceManager.putString(
                        Constants.KEY_APP_LANGUAGE,
                        Constants.KEY_LANGUAGE_ENGLISH
                    )
                } else if (deviceLanguage == Constants.KEY_LANGUAGE_ARABIC) {
                    mPreferenceManager.putString(
                        Constants.KEY_APP_LANGUAGE,
                        Constants.KEY_LANGUAGE_ARABIC
                    )
                }
                if (darkModeState == Constants.KEY_DARK_MODE) {
                    mPreferenceManager.putString(
                        Constants.KEY_APP_DARK_MODE_STATE,
                        Constants.KEY_DARK_MODE
                    )
                } else {
                    mPreferenceManager.putString(
                        Constants.KEY_APP_DARK_MODE_STATE,
                        Constants.KEY_LIGHT_MODE
                    )
                }

                runBlocking {
                    mSignOutState.emit(Constants.KEY_TRUE_RETURN)
                }
            }
            .addOnFailureListener {
                runBlocking {
                    mSignOutState.emit(Constants.KEY_FALSE_RETURN)
                }
            }

        return mSignOutState
    }
}