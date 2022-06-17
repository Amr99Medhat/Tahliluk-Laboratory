package com.amrmedhatandroid.tahliluk_laboratory.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.adapters.ChatAdapter
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ActivityChatBinding
import com.amrmedhatandroid.tahliluk_laboratory.models.ChatMessage
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.ChatViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : BaseActivity() {
    private lateinit var mActivityChatBinding: ActivityChatBinding
    private var mChatMessages: ArrayList<ChatMessage> = ArrayList()
    private lateinit var mChatAdapter: ChatAdapter
    private lateinit var mChatViewModel: ChatViewModel
    private lateinit var mReceiverImage: Bitmap
    private lateinit var mReceiverImageString: String
    private var mReceiverName: String? = null
    private var mReceiverId: String? = null
    private var mLabId: String? = null
    private var mLabName: String? = null
    private lateinit var mPatientToken: String
    private lateinit var mLabImageString: String
    private var mConversionId: String? = null
    private var mIsReceiverAvailable = 0
    private var mImageError = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityChatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(mActivityChatBinding.root)
        mChatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        loadReceiverDetails(intent)
        init()
        setListeners()
        listenMessages()
    }

    private fun init() {
        lifecycleScope.launchWhenResumed {
            mChatViewModel.getLabId(this@ChatActivity).collect {
                mLabId = it
            }
        }
        lifecycleScope.launchWhenResumed {
            mChatViewModel.getLabName(this@ChatActivity).collect {
                mLabName = it
            }
        }
        lifecycleScope.launchWhenResumed {
            mChatViewModel.getLabImage(this@ChatActivity).collect {
                mLabImageString = it
            }
        }
        lifecycleScope.launchWhenResumed {
            if (mLabId != null)
                mChatAdapter = ChatAdapter(
                    mLabId!!,
                    mChatMessages,
                    mReceiverImage
                )
            mActivityChatBinding.chatRecyclerView.adapter = mChatAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadReceiverDetails(intent: Intent) {
        lifecycleScope.launchWhenResumed {
            mChatViewModel.getPatientName(intent).collect {
                mReceiverName = it
                mActivityChatBinding.patientTextName.text = mReceiverName
            }
        }
        lifecycleScope.launch {
            mChatViewModel.gePatientImage(intent).collect {
                val bitmap = SupportClass.getBitmapFromBytes(it)
                if (bitmap != null) {
                    mReceiverImage = bitmap
                    mActivityChatBinding.patientImage.setImageBitmap(mReceiverImage)
                } else {
                    mImageError = 1
                    mReceiverImage = BitmapFactory.decodeResource(
                        resources, R.drawable.ic_person
                    )
                    mActivityChatBinding.patientImage.setImageBitmap(mReceiverImage)
                }

            }
        }
        lifecycleScope.launchWhenResumed {
            mChatViewModel.getPatientId(intent).collect {
                mReceiverId = it
            }
        }

        lifecycleScope.launchWhenResumed {
            mChatViewModel.getPatientImageString(intent).collect {
                mReceiverImageString = it
            }
        }
    }

    private suspend fun reloadReceiverImage() {
        mChatViewModel.reloadReceiverImage(mReceiverId!!).collect {
            try {
                val reloadedBitmap = SupportClass.getBitmapFromBytes(it)
                mReceiverImage = reloadedBitmap!!
                mActivityChatBinding.patientImage.setImageBitmap(mReceiverImage)
                mChatAdapter = ChatAdapter(
                    mLabId!!,
                    mChatMessages,
                    mReceiverImage
                )
                mActivityChatBinding.chatRecyclerView.adapter = mChatAdapter
            } catch (ex: Exception) {
            }
        }
    }

    private fun setListeners() {
        mActivityChatBinding.imageBack.setOnClickListener {
            onBackPressed()
        }
        mActivityChatBinding.layoutSend.setOnClickListener {
            if (mActivityChatBinding.inputMessage.text.isNotEmpty()) {
                sendMessage()

            }
        }
    }

    private fun sendMessage() {
        lifecycleScope.launchWhenResumed {
            val message: HashMap<Any, Any> = HashMap()
            message[Constants.KEY_SENDER_ID] = mLabId!!
            message[Constants.KEY_RECEIVER_ID] = mReceiverId!!
            message[Constants.KEY_MESSAGE] = mActivityChatBinding.inputMessage.text.toString()
            message[Constants.KEY_TIMESTAMP] = Date()

            mChatViewModel.sendMessage(
                message
            ).collect {
                when (it) {
                    Constants.KEY_FALSE_RETURN -> {
                        SupportClass.showToast(
                            applicationContext,
                            Constants.KEY_SEND_MESSAGE_FAILED
                        )
                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            if (mConversionId != null) {
                goUpdateConversion()
            } else {
                goAddConversion()
            }
        }
        if (mIsReceiverAvailable == 0) {
            goSendNotification()
        }
        mActivityChatBinding.inputMessage.text = null

    }

    private fun listenMessages() {
        lifecycleScope.launchWhenResumed {
            if (mReceiverId != null)
                mChatViewModel.listenMessages(mReceiverId!!, eventListener)
        }
    }

    private fun goSendNotification() {
        try {
            val tokens = JSONArray()
            tokens.put(mPatientToken)
            val data = JSONObject()
            data.put(
                "id",
                mLabId
            )
            data.put("name", mLabName)
            data.put(
                "fcmToken",
                mPatientToken
            )
            data.put("message", mActivityChatBinding.inputMessage.text.toString())

            val body = JSONObject()
            body.put(Constants.REMOTE_MSG_DATA, data)
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens)

            lifecycleScope.launch {
                mChatViewModel.sendNotification(body.toString()).collect {
                    when (it) {
                        Constants.KEY_ERROR -> {
                            Log.d("here-goSendNotification", "Can't send notification")
                        }
                        Constants.KEY_NOTIFICATION_SENT_SUCCESSFULLY -> {
                            Log.d(
                                "here-goSendNotification",
                                Constants.KEY_NOTIFICATION_SENT_SUCCESSFULLY
                            )
                        }
                        Constants.KEY_RESPONSE_FAILURE -> {
                            Log.d("here-goSendNotification", Constants.KEY_RESPONSE_FAILURE)
                        }
                        else -> {
                            Log.d("here-goSendNotification", it)
                        }
                    }
                }
            }
        } catch (exception: Exception) {
            SupportClass.showToast(applicationContext, exception.message!!.toString())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val eventListener: EventListener<QuerySnapshot> = EventListener { value, error ->
        if (error != null) {
            return@EventListener
        }
        if (value != null) {
            val count = mChatMessages.size
            for (documentChange in value.documentChanges) {
                if (documentChange.type == DocumentChange.Type.ADDED) {
                    val chatMessage = ChatMessage()
                    chatMessage.senderId =
                        documentChange.document.getString(Constants.KEY_SENDER_ID)
                    chatMessage.receiverId =
                        documentChange.document.getString(Constants.KEY_RECEIVER_ID)
                    chatMessage.message =
                        documentChange.document.getString(Constants.KEY_MESSAGE)
                    chatMessage.dateTime =
                        SupportClass.getReadableDateTime(documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!)
                    chatMessage.dateObject =
                        documentChange.document.getDate(Constants.KEY_TIMESTAMP)
                    mChatMessages.add(chatMessage)
                }
            }
            mChatMessages.sortWith { obj1: ChatMessage, obj2: ChatMessage ->
                obj1.dateObject!!.compareTo(obj2.dateObject)
            }
            if (count == 0) {
                mChatAdapter.notifyDataSetChanged()
            } else {
                mChatAdapter.notifyItemRangeInserted(mChatMessages.size, mChatMessages.size)
                mActivityChatBinding.chatRecyclerView.smoothScrollToPosition(mChatMessages.size - 1)
            }
            mActivityChatBinding.chatRecyclerView.visibility = View.VISIBLE
        }
        SupportClass.loading(false, null, mActivityChatBinding.progressBar)
        if (mConversionId == null) {
            checkForConversion()
        }
    }


    private fun listenAvailabilityOfReceiver() {
        lifecycleScope.launchWhenResumed {
            mChatViewModel.listenAvailabilityOfReceiver(this@ChatActivity, mReceiverId!!).collect {
                when (it) {
                    Constants.KEY_STRING_ZERO -> {
                        mIsReceiverAvailable = 0
                        mActivityChatBinding.textAvailability.visibility = View.GONE
                    }
                    Constants.KEY_STRING_ONE -> {
                        mIsReceiverAvailable = 1
                        mActivityChatBinding.textAvailability.visibility = View.VISIBLE
                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            receiverAvailable()
        }
        getTokenOfAvailableReceiver()

    }

    private suspend fun receiverAvailable() {
        if (mImageError == 1) {
            reloadReceiverImage()
        }
    }

    private fun getTokenOfAvailableReceiver() {
        lifecycleScope.launchWhenResumed {
            mChatViewModel.getTokenOfAvailableReceiver(mReceiverId!!).collect {
                if (it != "null")
                    mPatientToken = it
            }
        }
    }

    private fun checkForConversion() {
        if (mChatMessages.size != 0) {
            checkForConversionRemotely(
                mLabId!!,
                mReceiverId!!
            )
            checkForConversionRemotely(
                mReceiverId!!,
                mLabId!!
            )
        }
    }

    private fun goUpdateConversion() {
        lifecycleScope.launchWhenResumed {
            mChatViewModel.updateConversion(
                mConversionId!!,
                mActivityChatBinding.inputMessage.text.toString()
            ).collect {
                when (it) {
                    Constants.KEY_FALSE_RETURN -> {
                        SupportClass.showToast(this@ChatActivity, "Failed update Conversion")
                    }
                }
            }
        }
    }

    private fun goAddConversion() {
        val conversion: HashMap<String, Any> = HashMap()
        conversion[Constants.KEY_SENDER_ID] =
            mLabId!!
        conversion[Constants.KEY_SENDER_NAME] =
            mLabName!!
        conversion[Constants.KEY_SENDER_IMAGE] = mLabImageString
        conversion[Constants.KEY_RECEIVER_ID] = mReceiverId!!
        conversion[Constants.KEY_RECEIVER_NAME] = mReceiverName!!
        conversion[Constants.KEY_RECEIVER_IMAGE] = mReceiverImageString
        conversion[Constants.KEY_LAST_MESSAGE] = mActivityChatBinding.inputMessage.text.toString()
        conversion[Constants.KEY_TIMESTAMP] = Date()
        addConversion(conversion)
    }

    private fun addConversion(conversion: HashMap<String, Any>) {
        lifecycleScope.launchWhenResumed {
            mChatViewModel.addConversion(conversion).collect {
                mConversionId = it
            }
        }
    }

    private fun checkForConversionRemotely(senderId: String, receiverId: String) {
        lifecycleScope.launchWhenResumed {
            mChatViewModel.checkForConversionRemotely(
                senderId,
                receiverId,
                conversionOnCompleteListener
            )
        }
    }

    private val conversionOnCompleteListener: OnCompleteListener<QuerySnapshot> =
        OnCompleteListener {
            if (it.isSuccessful && it.result != null && it.result!!.documents.size > 0) {
                val documentSnapshot: DocumentSnapshot = it.result!!.documents[0]
                mConversionId = documentSnapshot.id
            }
        }

    override fun onResume() {
        super.onResume()
        listenAvailabilityOfReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mChatViewModel.viewModelScope.cancel()
    }
}