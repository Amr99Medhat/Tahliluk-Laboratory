package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

class BaseRepository {
    private val mDatabase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var mPreferenceManager: PreferenceManager
    private val mAvailabilityState: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)

    suspend fun setAvailabilityOnline(context: Context): MutableStateFlow<String> {
        mPreferenceManager = PreferenceManager(context)
        val dR = mDatabase.collection(Constants.KEY_COLLECTION_LABS)
            .document(mPreferenceManager.getString(Constants.KEY_LAB_ID)!!)
        val x = dR.update(Constants.KEY_AVAILABILITY, Constants.KEY_ONE)
        x.addOnFailureListener {
            runBlocking { mAvailabilityState.emit(Constants.KEY_FALSE_RETURN) }
        }
        return mAvailabilityState
    }

    suspend fun setAvailabilityOffline(context: Context): MutableStateFlow<String> {
        mPreferenceManager = PreferenceManager(context)
        val dR = mDatabase.collection(Constants.KEY_COLLECTION_LABS)
            .document(mPreferenceManager.getString(Constants.KEY_LAB_ID)!!)
        val x = dR.update(Constants.KEY_AVAILABILITY, Constants.KEY_ZERO)
        x.addOnFailureListener {
            runBlocking { mAvailabilityState.emit(Constants.KEY_FALSE_RETURN) }
        }
        return mAvailabilityState
    }
}