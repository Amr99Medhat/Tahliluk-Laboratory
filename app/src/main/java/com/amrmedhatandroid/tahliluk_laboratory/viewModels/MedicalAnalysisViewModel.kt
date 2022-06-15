package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.amrmedhatandroid.tahliluk_laboratory.models.Analytics
import com.amrmedhatandroid.tahliluk_laboratory.repositories.MedicalAnalysisRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.ArrayList

class MedicalAnalysisViewModel:ViewModel() {
    private val mMedicalAnalysisRepository: MedicalAnalysisRepository = MedicalAnalysisRepository()

    suspend fun getMedicalAnalysis(labId:String,collectionName:String): MutableStateFlow<ArrayList<Analytics>> {
        return mMedicalAnalysisRepository.getAnalytics(labId,collectionName)
    }

     fun deleteMedicalAnalysis(context: Context,collectionName:String,analysisList:ArrayList<Analytics>){
        mMedicalAnalysisRepository.deleteMedicalAnalysis(context,collectionName,analysisList)
    }

}