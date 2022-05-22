package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.firebase.FirestoreClass
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

class MainRepository {
    private lateinit var mPreferenceManager: PreferenceManager
    private var mGetTokenState: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mUpdateTokenState: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)

    suspend fun getToken(): MutableStateFlow<String> {
        mGetTokenState = MutableStateFlow(Constants.KEY_EMPTY)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            runBlocking {
                mGetTokenState.emit(it)
            }
        }
        return mGetTokenState
    }

    suspend fun updateToken(
        context: Context,
        token: String
    ): MutableStateFlow<String> {
        mUpdateTokenState = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        mPreferenceManager.putString(Constants.KEY_FCM_TOKEN, token)
        val task = FirestoreClass().updateToken(
            Constants.KEY_COLLECTION_LABS,
            mPreferenceManager.getString(Constants.KEY_LAB_ID)!!,
            token
        )
        task.addOnFailureListener {
            runBlocking {
                mUpdateTokenState.emit(Constants.KEY_FALSE_RETURN)
            }
        }
        return mUpdateTokenState
    }

    //TODO("setDeviceLanguage into repo")
//    private fun setDeviceLanguage() {
//        if (preferenceManager.getString(Constants.KEY_DEVICE_LANGUAGE) == null) {
//            when (Locale.getDefault().displayLanguage) {
//                Constants.KEY_LANGUAGE_ENGLISH_SYSTEM -> {
//                    preferenceManager.putString(
//                        Constants.KEY_DEVICE_LANGUAGE,
//                        Constants.KEY_LANGUAGE_ENGLISH_SYSTEM
//                    )
//                }
//                Constants.KEY_LANGUAGE_ARABIC_SYSTEM -> {
//                    preferenceManager.putString(
//                        Constants.KEY_DEVICE_LANGUAGE,
//                        Constants.KEY_LANGUAGE_ARABIC_SYSTEM
//                    )
//                }
//                else -> {
//                    preferenceManager.putString(
//                        Constants.KEY_DEVICE_LANGUAGE,
//                        Constants.KEY_LANGUAGE_ENGLISH_SYSTEM
//                    )
//                }
//            }
//        } else {
//            val deviceLanguage = preferenceManager.getString(Constants.KEY_DEVICE_LANGUAGE)
//            if (deviceLanguage == Constants.KEY_LANGUAGE_ENGLISH_SYSTEM) {
//                SupportFunctions.setLocale(Constants.KEY_LANGUAGE_ENGLISH, resources)
//            } else if (deviceLanguage == Constants.KEY_LANGUAGE_ARABIC_SYSTEM) {
//                SupportFunctions.setLocale(Constants.KEY_LANGUAGE_ARABIC, resources)
//            }
//        }
//    }

}