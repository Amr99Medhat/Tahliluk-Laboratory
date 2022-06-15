package com.amrmedhatandroid.tahliluk_laboratory.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.amrmedhatandroid.tahliluk_laboratory.models.Analytics
import com.amrmedhatandroid.tahliluk_laboratory.repositories.EditMedicalAnalysisFragmentRepository
import com.amrmedhatandroid.tahliluk_laboratory.repositories.MedicalAnalysisRepository
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.ArrayList

class EditMedicalAnalysisFragmentViewModel:ViewModel() {

    private val mEditMedicalAnalysisRepository: EditMedicalAnalysisFragmentRepository = EditMedicalAnalysisFragmentRepository()

    suspend fun getMedicalAnalysis(context:Context,collectionName:String): MutableStateFlow<ArrayList<Analytics>> {
        return mEditMedicalAnalysisRepository.getAnalytics(context,collectionName)
    }

    fun editMedicalAnalysis(context: Context, collectionName:String, analysisList: ArrayList<Analytics>){
        mEditMedicalAnalysisRepository.editMedicalAnalysis(context,collectionName,analysisList)
    }
}