package com.amrmedhatandroid.tahliluk_laboratory.repositories

import com.amrmedhatandroid.tahliluk_laboratory.firebase.FirestoreClass
import com.amrmedhatandroid.tahliluk_laboratory.models.Patient
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.*

class PatientsRepository {
    private var mPatientsArrayList: MutableStateFlow<ArrayList<Patient>> =
        MutableStateFlow(ArrayList())

    suspend fun getPatients(): MutableStateFlow<ArrayList<Patient>> {
        mPatientsArrayList = MutableStateFlow(ArrayList())
        FirestoreClass().getPatients(Constants.KEY_COLLECTION_PATIENTS).addOnSuccessListener {
            val patientsList: ArrayList<Patient> = ArrayList()
            for (labObject in it.documents) {
                val patient = labObject.toObject(Patient::class.java)
                if (patient != null) {
                    patient.id = labObject.id
                    patientsList.add(patient)
                }
            }
            runBlocking { mPatientsArrayList.emit(patientsList) }
        }
        return mPatientsArrayList
    }
}