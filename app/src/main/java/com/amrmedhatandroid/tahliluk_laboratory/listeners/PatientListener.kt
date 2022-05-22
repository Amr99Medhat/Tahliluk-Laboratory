package com.amrmedhatandroid.tahliluk_laboratory.listeners

import com.amrmedhatandroid.tahliluk_laboratory.models.Patient

interface PatientListener {
    fun onPatientClicked(patient: Patient)
}