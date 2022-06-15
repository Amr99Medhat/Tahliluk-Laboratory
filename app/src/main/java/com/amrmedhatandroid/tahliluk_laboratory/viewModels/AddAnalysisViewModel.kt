package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.amrmedhatandroid.tahliluk_laboratory.models.Analytics

import com.amrmedhatandroid.tahliluk_laboratory.repositories.AddMedicalAnalysisRepository


class AddAnalysisViewModel :ViewModel() {
    private val mAddMedicalAnalysisRepository = AddMedicalAnalysisRepository()
    suspend fun addMedicalAnalysis(context: Context, collectionName:String, analysis: Analytics) {
       mAddMedicalAnalysisRepository.addAnalytics(context,collectionName,analysis)
    }
}