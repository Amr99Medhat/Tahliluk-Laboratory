package com.amrmedhatandroid.tahliluk_laboratory.repositories

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.firebase.FirestoreClass
import com.amrmedhatandroid.tahliluk_laboratory.models.Analytics
import com.amrmedhatandroid.tahliluk_laboratory.models.Reserve
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.ArrayList

class AddMedicalAnalysisRepository {
    private lateinit var mPreferenceManager: PreferenceManager
    suspend fun addAnalytics(context: Context,collectionName:String,analysis:Analytics){
        mPreferenceManager = PreferenceManager(context)
        val labId = mPreferenceManager.getString(Constants.KEY_LAB_ID)!!
        val analysisHashMap = HashMap<String,String>()
        analysisHashMap[Constants.ANALYSIS_NAME] = analysis.analysis_name!!
        analysisHashMap[Constants.ANALYSIS_PRICE] = analysis.analysis_price!!
        FirestoreClass().updateMedicalAnalytics(collectionName,labId).update(Constants.LAB_ANALYTICS, FieldValue.arrayUnion(analysisHashMap)).addOnSuccessListener {
            Toast.makeText(context,context.getString(R.string.Analysis_added_successfully), Toast.LENGTH_LONG).show()
        }
    }
}