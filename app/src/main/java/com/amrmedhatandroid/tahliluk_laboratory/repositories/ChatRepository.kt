package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import android.content.Intent
import android.util.Base64
import com.amrmedhatandroid.tahliluk_laboratory.activities.ChatActivity
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.firebase.FirestoreClass
import com.amrmedhatandroid.tahliluk_laboratory.models.Patient
import com.amrmedhatandroid.tahliluk_laboratory.network.ApiClient
import com.amrmedhatandroid.tahliluk_laboratory.network.ApiService
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.NotNull
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRepository {
    private lateinit var mPreferenceManager: PreferenceManager
    private var mPatientName: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mLabId: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mLabName: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mSendMessage: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mPatientId: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mConversionId: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mAddConversionState: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mLabImageString: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mLabPatientString: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mNotificationState: MutableStateFlow<String> = MutableStateFlow(Constants.KEY_EMPTY)
    private var mListenAvailabilityOfReceiver: MutableStateFlow<String> =
        MutableStateFlow(Constants.KEY_EMPTY)
    private var mAvailabilityOfReceiverToken: MutableStateFlow<String> =
        MutableStateFlow(Constants.KEY_EMPTY)
    private var mReloadReceiverImage: MutableStateFlow<ByteArray> =
        MutableStateFlow(byteArrayOf(0x2E, 0x38))
    private var mPatientImageBytes: MutableStateFlow<ByteArray> =
        MutableStateFlow(byteArrayOf(0x2E, 0x38))


    suspend fun getPatientName(intent: Intent): MutableStateFlow<String> {
        mPatientName = MutableStateFlow(Constants.KEY_EMPTY)
        val receiverPatient = intent.getSerializableExtra(Constants.KEY_PATIENT) as Patient
        mPatientName.emit("${receiverPatient.firstName} ${receiverPatient.lastName}")
        return mPatientName
    }

    suspend fun getPatientImage(intent: Intent): MutableStateFlow<ByteArray> {
        mPatientImageBytes = MutableStateFlow(byteArrayOf(0x2E, 0x38))
        val receiverPatient = intent.getSerializableExtra(Constants.KEY_PATIENT) as Patient
        val bytes: ByteArray =
            Base64.decode(receiverPatient.image, Base64.DEFAULT)
        mPatientImageBytes.emit(bytes)
        return mPatientImageBytes
    }

    suspend fun getPatientImageString(intent: Intent): MutableStateFlow<String> {
        mLabPatientString = MutableStateFlow(Constants.KEY_EMPTY)
        val receiverPatient = intent.getSerializableExtra(Constants.KEY_PATIENT) as Patient
        mLabPatientString.emit(receiverPatient.image)
        return mLabPatientString
    }

    suspend fun getPatientId(intent: Intent): MutableStateFlow<String> {
        mPatientId = MutableStateFlow(Constants.KEY_EMPTY)
        val receiverPatient = intent.getSerializableExtra(Constants.KEY_PATIENT) as Patient
        mPatientId.emit(receiverPatient.id)
        return mPatientId
    }

    suspend fun getLabId(context: Context): MutableStateFlow<String> {
        mLabId = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        mLabId.emit(mPreferenceManager.getString(Constants.KEY_LAB_ID)!!)
        return mLabId
    }

    suspend fun getLabName(context: Context): MutableStateFlow<String> {
        mLabName = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        mLabName.emit(mPreferenceManager.getString(Constants.KEY_LAB_NAME)!!)
        return mLabName
    }

    suspend fun getLabImage(context: Context): MutableStateFlow<String> {
        mLabImageString = MutableStateFlow(Constants.KEY_EMPTY)
        mPreferenceManager = PreferenceManager(context)
        mLabImageString.emit(mPreferenceManager.getString(Constants.KEY_IMAGE)!!)
        return mLabImageString
    }

    suspend fun sendMessage(
        message: HashMap<Any, Any>
    ): MutableStateFlow<String> {
        mSendMessage = MutableStateFlow(Constants.KEY_EMPTY)
        FirestoreClass().sendMessage(
            Constants.KEY_COLLECTION_CHAT,
            message
        ).addOnFailureListener {
            runBlocking { mSendMessage.emit(Constants.KEY_FALSE_RETURN) }
        }
        return mSendMessage
    }

    fun listenMessages(
        receiverPatient: String,
        eventListener: EventListener<QuerySnapshot>
    ) {
        FirestoreClass().listenMessages(
            Constants.KEY_COLLECTION_CHAT,
            Constants.KEY_SENDER_ID,
            mPreferenceManager.getString(Constants.KEY_LAB_ID)!!,
            Constants.KEY_RECEIVER_ID,
            receiverPatient,
            eventListener
        )
    }

    fun checkForConversionRemotely(
        senderId: String,
        receiverId: String,
        conversionOnCompleteListener: OnCompleteListener<QuerySnapshot>
    ) {
        FirestoreClass().checkForConversionRemotely(
            Constants.KEY_COLLECTION_CONVERSATIONS,
            Constants.KEY_SENDER_ID,
            senderId,
            Constants.KEY_RECEIVER_ID,
            receiverId,
            conversionOnCompleteListener
        )
    }

    suspend fun addConversion(
        conversion: HashMap<String, Any>
    ): MutableStateFlow<String> {
        mConversionId = MutableStateFlow(Constants.KEY_EMPTY)
        FirestoreClass().addConversion(Constants.KEY_COLLECTION_CONVERSATIONS, conversion)
            .addOnSuccessListener {
                runBlocking { mConversionId.emit(it.id) }
            }
        return mConversionId
    }

    suspend fun updateConversion(
        conversionId: String,
        message: String,
    ): MutableStateFlow<String> {
        mAddConversionState = MutableStateFlow(Constants.KEY_EMPTY)
        FirestoreClass().updateConversion(
            Constants.KEY_COLLECTION_CONVERSATIONS,
            conversionId,
            Constants.KEY_LAST_MESSAGE,
            message,
            Constants.KEY_TIMESTAMP
        ).addOnFailureListener {
            runBlocking { mAddConversionState.emit(Constants.KEY_FALSE_RETURN) }
        }
        return mAddConversionState
    }

    suspend fun listenAvailabilityOfReceiver(
        activity: ChatActivity,
        receiverPatientId: String
    ): MutableStateFlow<String> {
        mListenAvailabilityOfReceiver = MutableStateFlow(Constants.KEY_EMPTY)
        FirestoreClass().listenAvailabilityOfReceiver(
            Constants.KEY_COLLECTION_PATIENTS,
            receiverPatientId
        ).addSnapshotListener(activity)
        { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (value != null) {
                runBlocking {
                    mListenAvailabilityOfReceiver.emit(
                        value.getLong(Constants.KEY_AVAILABILITY).toString()
                    )
                }
            }
        }
        return mListenAvailabilityOfReceiver
    }

    suspend fun reloadReceiverImage(
        receiverPatientId: String
    ): MutableStateFlow<ByteArray> {
        mReloadReceiverImage = MutableStateFlow(byteArrayOf(0x2E, 0x38))
        FirestoreClass().listenAvailabilityOfReceiver(
            Constants.KEY_COLLECTION_PATIENTS,
            receiverPatientId
        ).addSnapshotListener()
        { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (value != null) {
                val bytes: ByteArray =
                    Base64.decode(
                        value.getString(Constants.KEY_IMAGE).toString(), Base64.DEFAULT
                    )
                runBlocking { mReloadReceiverImage.emit(bytes) }
            }
        }
        return mReloadReceiverImage
    }

    suspend fun getTokenOfAvailableReceiver(
        receiverPatientId: String
    ): MutableStateFlow<String> {
        mAvailabilityOfReceiverToken = MutableStateFlow(Constants.KEY_EMPTY)
        FirestoreClass().listenAvailabilityOfReceiver(
            Constants.KEY_COLLECTION_PATIENTS,
            receiverPatientId
        ).addSnapshotListener()
        { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (value != null) {
                runBlocking {
                    mAvailabilityOfReceiverToken.emit(
                        value.getString(Constants.KEY_FCM_TOKEN).toString()
                    )
                }
            }
        }
        return mAvailabilityOfReceiverToken
    }

    suspend fun sendNotification(messageBody: String): MutableStateFlow<String> {
        mNotificationState = MutableStateFlow(Constants.KEY_EMPTY)
        ApiClient.getClient()?.create(ApiService::class.java)?.sendMessage(
            Constants.getRemoteMsgHeaders(),
            messageBody
        )?.enqueue(object : Callback<String> {
            override fun onResponse(
                @NotNull call: Call<String>,
                @NotNull response: Response<String>
            ) {
                if (response.isSuccessful) {
                    try {
                        if (response.body() != null) {
                            val responseJson = JSONObject(response.body()!!)
                            //val results: JSONArray = responseJson.getJSONArray("results")
                            if (responseJson.getInt(Constants.KEY_FAILURE) == Constants.KEY_ONE) {
                                //val error: JSONObject = results.get(0) as JSONObject
                                runBlocking { mNotificationState.emit(Constants.KEY_ERROR) }
                                return
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    runBlocking { mNotificationState.emit(Constants.KEY_NOTIFICATION_SENT_SUCCESSFULLY) }
                } else {
                    runBlocking { mNotificationState.emit(Constants.KEY_RESPONSE_FAILURE) }
                }
            }

            override fun onFailure(@NotNull call: Call<String>, @NotNull t: Throwable) {
                runBlocking { mNotificationState.emit(t.message.toString()) }
            }
        })
        return mNotificationState
    }
}