package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import android.util.Base64
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import kotlinx.coroutines.flow.MutableStateFlow

class HomeFragmentRepository {
    private lateinit var mPreferenceManager: PreferenceManager
    private var mLabName: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
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

}