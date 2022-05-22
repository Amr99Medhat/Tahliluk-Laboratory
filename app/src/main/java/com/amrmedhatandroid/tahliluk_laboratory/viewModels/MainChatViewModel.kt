package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.repositories.MainChatRepository
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainChatViewModel : ViewModel() {
    private val mMainChatRepository: MainChatRepository = MainChatRepository()

    suspend fun getLabName(context: Context): MutableStateFlow<String> {
        return mMainChatRepository.getLabName(context)
    }

    suspend fun getLabImage(context: Context): MutableStateFlow<ByteArray> {
        return mMainChatRepository.getLabImage(context)
    }

    suspend fun getLabId(context: Context): MutableStateFlow<String> {
        return mMainChatRepository.getLabId(context)
    }

    fun listenConversations(
        labId: String,
        eventListener: EventListener<QuerySnapshot>
    ) {
        viewModelScope.launch {
            mMainChatRepository.listenConversations(labId, eventListener)
        }
    }
}