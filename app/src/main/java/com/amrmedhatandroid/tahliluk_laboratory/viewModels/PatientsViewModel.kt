package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import androidx.lifecycle.ViewModel
import com.amrmedhatandroid.tahliluk_laboratory.models.Patient
import com.amrmedhatandroid.tahliluk_laboratory.repositories.PatientsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.ArrayList

class PatientsViewModel : ViewModel() {
    private val mPatientsRepository: PatientsRepository = PatientsRepository()

    suspend fun getPatients(): MutableStateFlow<ArrayList<Patient>> {
        return mPatientsRepository.getPatients()
    }
}