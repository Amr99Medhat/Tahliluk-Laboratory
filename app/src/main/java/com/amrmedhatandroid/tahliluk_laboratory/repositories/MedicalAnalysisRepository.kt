package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.firebase.FirestoreClass
import com.amrmedhatandroid.tahliluk_laboratory.models.Analytics
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.ArrayList

class MedicalAnalysisRepository {

    private var mAnalyticsArrayList: MutableStateFlow<ArrayList<Analytics>> =
        MutableStateFlow(ArrayList())
    private lateinit var mPreferenceManager: PreferenceManager



    suspend fun getAnalytics(
        labId: String,
        collectionName: String
    ): MutableStateFlow<ArrayList<Analytics>> {
        var list: ArrayList<HashMap<String, String>>
        val medicalAnalytics = ArrayList<Analytics>()
        FirestoreClass().getLabAnalysis(labId, collectionName).addOnSuccessListener {
            list = it.get(Constants.LAB_ANALYTICS) as ArrayList<HashMap<String, String>>
            for (analysiss in list) {
                val analysis = Analytics()
                analysis.analysis_name = analysiss[Constants.ANALYSIS_NAME]
                analysis.analysis_price = analysiss[Constants.ANALYSIS_PRICE]
                medicalAnalytics.add(analysis)
            }

            runBlocking { mAnalyticsArrayList.emit(medicalAnalytics) }
        }
        return mAnalyticsArrayList
    }


     fun deleteMedicalAnalysis(context: Context,collectionName:String,analysisList:ArrayList<Analytics>){
         mPreferenceManager = PreferenceManager(context)
         val labId = mPreferenceManager.getString(Constants.KEY_LAB_ID)!!
        val hash = HashMap<String,ArrayList<Analytics>>()
        hash[Constants.LAB_ANALYTICS] = analysisList
        FirestoreClass().deleteMedicalAnalysis(collectionName,labId).update(hash as Map<String, Any>).addOnSuccessListener {

        }
    }
}
