package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.amrmedhatandroid.tahliluk_laboratory.fragments.ReservationDetailsFragment
import com.amrmedhatandroid.tahliluk_laboratory.models.Reserve
import com.amrmedhatandroid.tahliluk_laboratory.repositories.PatientsRepository
import com.amrmedhatandroid.tahliluk_laboratory.repositories.ReservationDetailsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.ArrayList

class ReservationsDetailsViewModel : ViewModel() {
    private val mReservationDetailsRepository: ReservationDetailsRepository = ReservationDetailsRepository()

     suspend fun uploadResult(fragment: ReservationDetailsFragment, imageFileUri: Uri?, imageType:String){
         mReservationDetailsRepository.uploadResult(fragment,imageFileUri,imageType)
    }

    suspend fun updateReservation(fragment:ReservationDetailsFragment,reservationId:String,reserveMap:HashMap<String,Any>){
        mReservationDetailsRepository.updateReservation(fragment,reservationId,reserveMap)
    }
}