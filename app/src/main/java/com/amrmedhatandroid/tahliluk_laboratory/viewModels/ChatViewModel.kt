package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.activities.ChatActivity
import com.amrmedhatandroid.tahliluk_laboratory.repositories.ChatRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val mChatRepository: ChatRepository = ChatRepository()

    suspend fun getPatientName(intent: Intent): MutableStateFlow<String> {
        return mChatRepository.getPatientName(intent)
    }

    suspend fun gePatientImage(intent: Intent): MutableStateFlow<ByteArray> {
        return mChatRepository.getPatientImage(intent)
    }

    suspend fun getPatientImageString(intent: Intent): MutableStateFlow<String> {
        return mChatRepository.getPatientImageString(intent)
    }

    suspend fun getPatientId(intent: Intent): MutableStateFlow<String> {
        return mChatRepository.getPatientId(intent)
    }

    suspend fun getLabId(context: Context): MutableStateFlow<String> {
        return mChatRepository.getLabId(context)
    }

    suspend fun getLabName(context: Context): MutableStateFlow<String> {
        return mChatRepository.getLabName(context)
    }

    suspend fun getLabImage(context: Context): MutableStateFlow<String> {
        return mChatRepository.getLabImage(context)
    }

    suspend fun sendMessage(
        message: HashMap<Any, Any>
    ): MutableStateFlow<String> {
        return mChatRepository.sendMessage(message)
    }

    fun listenMessages(
        receiverPatient: String,
        eventListener: EventListener<QuerySnapshot>
    ) {
        viewModelScope.launch {
            mChatRepository.listenMessages(receiverPatient, eventListener)
        }
    }

    fun checkForConversionRemotely(
        senderId: String,
        receiverId: String,
        conversionOnCompleteListener: OnCompleteListener<QuerySnapshot>
    ) {
        viewModelScope.launch {
            mChatRepository.checkForConversionRemotely(
                senderId,
                receiverId,
                conversionOnCompleteListener
            )
        }
    }

    suspend fun addConversion(conversion: HashMap<String, Any>): MutableStateFlow<String> {
        return mChatRepository.addConversion(conversion)
    }

    suspend fun updateConversion(
        conversionId: String,
        message: String,
    ): MutableStateFlow<String> {
        return mChatRepository.updateConversion(conversionId, message)
    }

    suspend fun listenAvailabilityOfReceiver(
        activity: ChatActivity,
        receiverPatientId: String
    ): MutableStateFlow<String> {
        return mChatRepository.listenAvailabilityOfReceiver(activity, receiverPatientId)
    }

    suspend fun reloadReceiverImage(
        receiverPatientId: String
    ): MutableStateFlow<ByteArray> {
        return mChatRepository.reloadReceiverImage(receiverPatientId)
    }

    suspend fun getTokenOfAvailableReceiver(
        receiverPatientId: String
    ): MutableStateFlow<String> {
        return mChatRepository.getTokenOfAvailableReceiver(receiverPatientId)
    }

    suspend fun sendNotification(messageBody: String): MutableStateFlow<String> {
        return mChatRepository.sendNotification(messageBody)
    }

}