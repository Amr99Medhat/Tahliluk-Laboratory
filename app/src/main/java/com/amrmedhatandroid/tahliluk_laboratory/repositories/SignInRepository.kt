package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.firebase.FirestoreClass
import com.amrmedhatandroid.tahliluk_laboratory.models.Lab
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

class SignInRepository {
    private lateinit var mPreferenceManager: PreferenceManager
    private var mSignInResult: MutableStateFlow<Lab> = MutableStateFlow(Lab())
    private var mLab: Lab = Lab()

    suspend fun signIn(
        inputNumber: String,
        inputPassword: String
    ): MutableStateFlow<Lab> {
        val task = FirestoreClass().signIn(
            Constants.KEY_COLLECTION_LABS,
            Constants.KEY_LAB_PHONE_NUMBER,
            inputNumber,
            Constants.KEY_PASSWORD,
            inputPassword
        )
        mSignInResult = MutableStateFlow(Lab())
        mLab = Lab()
        task.addOnCompleteListener {
            if (it.isSuccessful && it.result != null && it.result!!.documents.size > 0) {
                runBlocking {
                    val documentSnapshot = it.result!!.documents[0]
                    mLab.labId = documentSnapshot.id
                    mLab.labName = documentSnapshot.getString(Constants.KEY_LAB_NAME)!!
                    mLab.labImage = documentSnapshot.getString(Constants.KEY_IMAGE)!!
                    mLab.labPhoneNumber =
                        documentSnapshot.getString(Constants.KEY_LAB_PHONE_NUMBER)!!
                    mLab.labPassword = documentSnapshot.getString(Constants.KEY_PASSWORD)!!
                    mLab.labLatitude = documentSnapshot.getString(Constants.KEY_LATITUDE)!!
                    mLab.labLongitude = documentSnapshot.getString(Constants.KEY_LONGITUDE)!!
                    mLab.labVerifiedState =
                        documentSnapshot.getString(Constants.KEY_LAB_VERIFICATION_STATE)!!

                    mSignInResult.emit(mLab)
                }

            } else {
                runBlocking {
                    mLab.labId = "-1"
                    mLab.labName = "-1"
                    mLab.labImage = "-1"
                    mLab.labPhoneNumber = "-1"
                    mLab.labPassword = "-1"
                    mLab.labLatitude = "-1"
                    mLab.labLongitude = "-1"
                    mLab.labVerifiedState = "-1"

                    mSignInResult.emit(mLab)
                }
            }
        }
        return mSignInResult
    }


    fun saveBasicData(
        context: Context,
        lab: Lab
    ) {
        mPreferenceManager = PreferenceManager(context)
        mPreferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
        mPreferenceManager.putString(Constants.KEY_LAB_ID, lab.labId)
        mPreferenceManager.putString(Constants.KEY_IMAGE, lab.labImage)
        mPreferenceManager.putString(Constants.KEY_LAB_NAME, lab.labName)
        mPreferenceManager.putString(Constants.KEY_LAB_PHONE_NUMBER, lab.labPhoneNumber)
        mPreferenceManager.putString(Constants.KEY_PASSWORD, lab.labPassword)
        mPreferenceManager.putString(Constants.KEY_LATITUDE, lab.labLatitude)
        mPreferenceManager.putString(Constants.KEY_LONGITUDE, lab.labLongitude)
        mPreferenceManager.putString(Constants.KEY_APP_LANGUAGE, Constants.KEY_LANGUAGE_ENGLISH)
        mPreferenceManager.putString(Constants.KEY_APP_DARK_MODE_STATE, Constants.KEY_LIGHT_MODE)
    }

}