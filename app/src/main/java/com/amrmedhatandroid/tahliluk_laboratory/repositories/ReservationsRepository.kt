package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import android.util.Log
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.firebase.FirestoreClass
import com.amrmedhatandroid.tahliluk_laboratory.models.Reserve
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.ArrayList

class ReservationsRepository {

    private var mReservationsArrayList: MutableStateFlow<ArrayList<Reserve>> =
        MutableStateFlow(ArrayList())
    private lateinit var mPreferenceManager: PreferenceManager


    suspend fun getReservations(context: Context): MutableStateFlow<ArrayList<Reserve>> {
        mPreferenceManager = PreferenceManager(context)
        mReservationsArrayList = MutableStateFlow(ArrayList())
        var labId = mPreferenceManager.getString(Constants.KEY_LAB_ID)
        Log.d("labid",labId!!)
        FirestoreClass().getReservations(Constants.KEY_COLLECTION_RESERVATION,labId!!).addOnSuccessListener {
            val reservationsList: ArrayList<Reserve> = ArrayList()
            for (labObject in it.documents) {
                val reservation = labObject.toObject(Reserve::class.java)
                if (reservation != null) {
                    reservation.orderId = labObject.id
                    reservationsList.add(reservation)
                }
            }
            runBlocking { mReservationsArrayList.emit(reservationsList) }
        }
        return mReservationsArrayList
    }
}