package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.amrmedhatandroid.tahliluk_laboratory.models.Reserve
import com.amrmedhatandroid.tahliluk_laboratory.repositories.ReservationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.ArrayList


class ReservationsViewModel:ViewModel() {
    private val mReservationsRepository: ReservationsRepository = ReservationsRepository()

    suspend fun getReservations(context: Context): MutableStateFlow<ArrayList<Reserve>> {
        return mReservationsRepository.getReservations(context)
    }


}