package com.amrmedhatandroid.tahliluk_laboratory.firebase

import android.app.Activity
import android.net.Uri
import androidx.fragment.app.Fragment
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.fragments.MedicalAnalyticsFragment
import com.amrmedhatandroid.tahliluk_laboratory.fragments.ReservationDetailsFragment
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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


    fun getReservations(collectionName: String,
        labId:String):
        Task<QuerySnapshot> {
        val qs = mFireStore.collection(collectionName).whereEqualTo(Constants.KEY_LAB_ID,labId)
        return qs.get()
    }



    fun uploadResultToCloudStorage(fragment:ReservationDetailsFragment, imageFileUri: Uri?, imageType:String){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType+ System.currentTimeMillis() +"." +SupportClass().getFileExtensions(fragment,imageFileUri)
        )
        sRef.putFile(imageFileUri!!).addOnSuccessListener {taskSnapShot ->
                taskSnapShot.metadata!!.reference!!.downloadUrl.toString()
            taskSnapShot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { Uri ->
                        fragment.imageUploadSuccess(Uri.toString())
                }
            }
                .addOnFailureListener { Exception ->

                }
        }


    fun updateReservation(fragment:ReservationDetailsFragment,reservationId:String,reserveMap:HashMap<String,Any>){
        mFireStore.collection(Constants.KEY_COLLECTION_RESERVATION)
            .document(reservationId)
            .update(reserveMap)
            .addOnSuccessListener {
                fragment.reservationUpdated()

            }
    }

    fun getLabAnalysis(
                       labId: String,
                       collectionName:String)
    :Task<DocumentSnapshot> {
        val qs = mFireStore.collection(collectionName).document(labId)
        return qs.get()

    }
    }


