package com.amrmedhatandroid.tahliluk_laboratory.firebase

import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.HashMap

class FirestoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mDocumentReference: DocumentReference
    private val mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun checkIfExist(
        collectionName: String,
        KeyNumber: String,
        inputNumber: String
    ): Task<QuerySnapshot> {
        val qS = mFireStore.collection(collectionName)
        val query = qS.whereEqualTo(KeyNumber, inputNumber)
        return query.get()
    }

    fun signInByCredentials(
        credential: PhoneAuthCredential
    ): Task<AuthResult> {
        return mFirebaseAuth.signInWithCredential(credential)
    }

    fun signUp(
        collectionName: String,
        lab: HashMap<Any, Any>,
    ): Task<DocumentReference> {
        val cR = mFireStore.collection(collectionName)
        return cR.add(lab)
    }

    fun signIn(
        collectionName: String,
        KeyNumber: String,
        inputNumber: String,
        KeyPassword: String,
        inputPassword: String
    ): Task<QuerySnapshot> {
        val qs = mFireStore.collection(collectionName)
        val query = qs.whereEqualTo(KeyNumber, inputNumber)
            .whereEqualTo(KeyPassword, inputPassword)
        return query.get()
    }

    fun updateToken(
        collectionName: String,
        labId: String,
        token: String
    ): Task<Void> {
        mDocumentReference =
            mFireStore.collection(collectionName).document(
                labId
            )
        return mDocumentReference.update(Constants.KEY_FCM_TOKEN, token)
    }

    fun getPatients(collectionName: String): Task<QuerySnapshot> {
        val qs = mFireStore.collection(collectionName)
        return qs.get()
    }

    fun sendMessage(
        collectionName: String,
        message: HashMap<Any, Any>
    ): Task<DocumentReference> {
        return mFireStore.collection(collectionName).add(message)
    }

    fun listenMessages(
        collectionName: String,
        senderIdKey: String,
        labIdKey: String,
        receiverIdKey: String,
        receiverPatient: String,
        eventListener: EventListener<QuerySnapshot>
    ) {
        mFireStore.collection(collectionName)
            .whereEqualTo(
                senderIdKey,
                labIdKey
            )
            .whereEqualTo(receiverIdKey, receiverPatient)
            .addSnapshotListener(eventListener)
        mFireStore.collection(collectionName)
            .whereEqualTo(senderIdKey, receiverPatient)
            .whereEqualTo(
                receiverIdKey,
                labIdKey
            )
            .addSnapshotListener(eventListener)
    }

    fun checkForConversionRemotely(
        collectionName: String,
        senderIdKey: String,
        senderId: String,
        receiverIdKey: String,
        receiverId: String,
        conversionOnCompleteListener: OnCompleteListener<QuerySnapshot>
    ) {
        mFireStore.collection(collectionName)
            .whereEqualTo(senderIdKey, senderId)
            .whereEqualTo(receiverIdKey, receiverId)
            .get()
            .addOnCompleteListener(conversionOnCompleteListener)
    }

    fun addConversion(
        collectionName: String,
        conversion: HashMap<String, Any>
    ): Task<DocumentReference> {
        val cR = mFireStore.collection(collectionName)
        return cR.add(conversion)
    }

    fun updateConversion(
        collectionName: String,
        conversionId: String,
        lastMessageKey: String,
        message: String,
        timeStampKey: String
    ): Task<Void> {
        mDocumentReference =
            mFireStore.collection(collectionName).document(conversionId)
        return mDocumentReference.update(
            lastMessageKey,
            message,
            timeStampKey,
            Date()
        )
    }

    fun listenConversations(
        collectionName: String,
        senderIdKey: String,
        patientIdKey: String,
        receiverIdKey: String,
        eventListener: EventListener<QuerySnapshot>
    ) {
        mFireStore.collection(collectionName)
            .whereEqualTo(
                senderIdKey,
                patientIdKey
            )
            .addSnapshotListener(eventListener)

        mFireStore.collection(collectionName)
            .whereEqualTo(
                receiverIdKey,
                patientIdKey
            )
            .addSnapshotListener(eventListener)
    }

    fun listenAvailabilityOfReceiver(
        collectionName: String,
        receiverPatientId: String
    ): DocumentReference {
        return mFireStore.collection(collectionName).document(receiverPatientId)
    }

    fun signOut(
        collectionName: String,
        labIdKey: String,
        tokenKey: String
    ): Task<Void> {
        mDocumentReference = mFireStore.collection(collectionName).document(labIdKey)
        val updates: HashMap<String, Any> = HashMap()
        updates[tokenKey] = FieldValue.delete()
        return mDocumentReference.update(updates)
    }
}
