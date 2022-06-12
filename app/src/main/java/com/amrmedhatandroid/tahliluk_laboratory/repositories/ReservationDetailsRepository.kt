package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.net.Uri
import androidx.fragment.app.Fragment
import com.amrmedhatandroid.tahliluk_laboratory.firebase.FirestoreClass
import com.amrmedhatandroid.tahliluk_laboratory.fragments.ReservationDetailsFragment

class ReservationDetailsRepository {


    suspend fun uploadResult(fragment: ReservationDetailsFragment, imageFileUri: Uri?, imageType:String) {
        FirestoreClass().uploadResultToCloudStorage(fragment,imageFileUri,imageType)
    }

    suspend fun updateReservation(fragment:ReservationDetailsFragment,reservationId:String,reserveMap:HashMap<String,Any>){

        FirestoreClass().updateReservation(fragment,reservationId,reserveMap)
    }
}