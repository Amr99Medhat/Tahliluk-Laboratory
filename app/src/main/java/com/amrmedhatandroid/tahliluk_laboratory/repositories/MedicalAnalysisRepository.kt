package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import android.util.Log
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



    suspend fun getAnalytics(
        context: Context,
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
            Log.d("name", medicalAnalytics.size.toString())

            runBlocking { mAnalyticsArrayList.emit(medicalAnalytics) }
        }
        return mAnalyticsArrayList
    }
}
