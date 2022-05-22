package com.amrmedhatandroid.tahliluk_laboratory.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.adapters.RecentConversationsAdapter
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ActivityMainChatBinding
import com.amrmedhatandroid.tahliluk_laboratory.listeners.ConversionListener
import com.amrmedhatandroid.tahliluk_laboratory.models.ChatMessage
import com.amrmedhatandroid.tahliluk_laboratory.models.Patient
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.MainChatViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect

class MainChatActivity : BaseActivity(), ConversionListener {
    private lateinit var mActivityMainChatBinding: ActivityMainChatBinding
    private lateinit var mConversions: ArrayList<ChatMessage>
    private lateinit var mConversationsAdapter: RecentConversationsAdapter
    private lateinit var mMainChatViewModel: MainChatViewModel
    private var mLabId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainChatBinding = ActivityMainChatBinding.inflate(layoutInflater)
        setContentView(mActivityMainChatBinding.root)
        mMainChatViewModel = ViewModelProvider(this)[MainChatViewModel::class.java]
        init()
        loadPatientDetails()
        setListeners()
        listenConversations()
    }

    private fun init() {
        lifecycleScope.launchWhenResumed {
            mMainChatViewModel.getLabId(this@MainChatActivity).collect {
                mLabId = it
            }
        }
        lifecycleScope.launchWhenResumed {
            mConversions = ArrayList()
            mConversationsAdapter = RecentConversationsAdapter(mConversions, this@MainChatActivity)
            mActivityMainChatBinding.conversationsRecyclerView.adapter = mConversationsAdapter
        }
    }

    private fun loadPatientDetails() {
        lifecycleScope.launchWhenResumed {
            mMainChatViewModel.getLabName(applicationContext).collect {
                mActivityMainChatBinding.labName.text = it
            }
        }
        lifecycleScope.launchWhenResumed {
            mMainChatViewModel.getLabImage(applicationContext).collect {
                val bitmap = SupportClass.getBitmapFromBytes(it)
                mActivityMainChatBinding.labImageProfile.setImageBitmap(bitmap)
            }
        }
    }

    private fun setListeners() {
        mActivityMainChatBinding.imageBack.setOnClickListener {
            onBackPressed()
        }
        mActivityMainChatBinding.fabNewChat.setOnClickListener {
            startLabsActivity()
        }
    }

    private fun listenConversations() {
        lifecycleScope.launchWhenResumed {
            mMainChatViewModel.listenConversations(mLabId!!, eventListener)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val eventListener: EventListener<QuerySnapshot> =
        EventListener<QuerySnapshot> { value, error ->
            if (error != null) {
                return@EventListener
            }
            if (value != null) {
                for (documentChange: DocumentChange in value.documentChanges) {
                    if (documentChange.type == DocumentChange.Type.ADDED) {
                        val senderId: String =
                            documentChange.document.getString(Constants.KEY_SENDER_ID)!!
                        val receiverId: String =
                            documentChange.document.getString(Constants.KEY_RECEIVER_ID)!!
                        val chatMessage = ChatMessage()
                        chatMessage.senderId = senderId
                        chatMessage.receiverId = receiverId
                        if (mLabId.equals(senderId)) {
                            chatMessage.conversionImage =
                                documentChange.document.getString(Constants.KEY_RECEIVER_IMAGE)
                            chatMessage.conversionName =
                                documentChange.document.getString(Constants.KEY_RECEIVER_NAME)
                            chatMessage.conversionId =
                                documentChange.document.getString(Constants.KEY_RECEIVER_ID)
                        } else {
                            chatMessage.conversionImage =
                                documentChange.document.getString(Constants.KEY_SENDER_IMAGE)
                            chatMessage.conversionName =
                                documentChange.document.getString(Constants.KEY_SENDER_NAME)
                            chatMessage.conversionId =
                                documentChange.document.getString(Constants.KEY_SENDER_ID)
                        }
                        chatMessage.message =
                            documentChange.document.getString(Constants.KEY_LAST_MESSAGE)
                        chatMessage.dateObject =
                            documentChange.document.getDate(Constants.KEY_TIMESTAMP)
                        mConversions.add(chatMessage)
                    } else if (documentChange.type == DocumentChange.Type.MODIFIED) {
                        for (i in 0..mConversions.size) {
                            val senderId: String =
                                documentChange.document.getString(Constants.KEY_SENDER_ID)!!
                            val receiverId: String =
                                documentChange.document.getString(Constants.KEY_RECEIVER_ID)!!
                            if (mConversions[i].senderId.equals(senderId) && mConversions[i].receiverId.equals(
                                    receiverId
                                )
                            ) {
                                mConversions[i].message =
                                    documentChange.document.getString(Constants.KEY_LAST_MESSAGE)
                                mConversions[i].dateObject =
                                    documentChange.document.getDate(Constants.KEY_TIMESTAMP)
                                break
                            }
                        }
                    }
                }
                mConversions.sortWith { obj1: ChatMessage, obj2: ChatMessage ->
                    obj2.dateObject!!.compareTo(obj1.dateObject)
                }
                mConversationsAdapter.notifyDataSetChanged()
                mActivityMainChatBinding.conversationsRecyclerView.smoothScrollToPosition(0)
                mActivityMainChatBinding.conversationsRecyclerView.visibility = View.VISIBLE
                SupportClass.loading(false, null, mActivityMainChatBinding.progressBar)
            }
        }


    private fun startLabsActivity() {
        SupportClass.startActivity(this, PatientsActivity::class.java)
    }

    override fun onConversionClicked(patient: Patient) {
        val intent = Intent(applicationContext, ChatActivity::class.java)
        intent.putExtra(Constants.KEY_PATIENT, patient)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mMainChatViewModel.viewModelScope.cancel()
    }
}