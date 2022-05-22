package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import android.util.Base64
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.firebase.FirestoreClass
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow

class MainChatRepository {
    private lateinit var mPreferenceManager: PreferenceManager
    private var mLabName: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mLabId: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mLabImageBytes: MutableStateFlow<ByteArray> =
        MutableStateFlow(byteArrayOf(0x2E, 0x38))

    suspend fun getLabName(context: Context): MutableStateFlow<String> {
        mLabName = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        val name = mPreferenceManager.getString(Constants.KEY_LAB_NAME)
        if (name != null) {
            mLabName.emit(name)
        }
        return mLabName
    }

    suspend fun getLabImage(context: Context): MutableStateFlow<ByteArray> {
        mLabImageBytes = MutableStateFlow(byteArrayOf(0x2E, 0x38))
        mPreferenceManager = PreferenceManager(context)
        val bytes: ByteArray =
            Base64.decode(mPreferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT)
        mLabImageBytes.emit(bytes)
        return mLabImageBytes
    }

    suspend fun getLabId(context: Context): MutableStateFlow<String> {
        mLabId = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        mLabId.emit(mPreferenceManager.getString(Constants.KEY_LAB_ID)!!)
        return mLabId
    }

    fun listenConversations(
        labId: String,
        eventListener: EventListener<QuerySnapshot>
    ) {
        FirestoreClass().listenConversations(
            Constants.KEY_COLLECTION_CONVERSATIONS,
            Constants.KEY_SENDER_ID,
            labId,
            Constants.KEY_RECEIVER_ID,
            eventListener
        )
    }
}