package com.amrmedhatandroid.tahliluk_laboratory.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.FragmentAddMedicalAnalysisBinding
import com.amrmedhatandroid.tahliluk_laboratory.models.Analytics
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.AddAnalysisViewModel
import kotlinx.coroutines.cancel


class AddMedicalAnalysisFragment : Fragment() {
    private lateinit var mAddMedicalAnalysisBinding: FragmentAddMedicalAnalysisBinding
    private lateinit var mAddMedicalAnalysisViewModel: AddAnalysisViewModel
    private lateinit var mAnalytics:Analytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAddMedicalAnalysisBinding = FragmentAddMedicalAnalysisBinding.inflate(layoutInflater)
        mAddMedicalAnalysisViewModel = ViewModelProvider(this)[AddAnalysisViewModel::class.java]
        setListeners()
        return mAddMedicalAnalysisBinding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddMedicalAnalysisFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }

    private fun setListeners(){
        mAddMedicalAnalysisBinding.btnSignIn.setOnClickListener {
            if (isValidAnalysisDetails()){
                mAnalytics = Analytics()
                mAnalytics.analysis_name = mAddMedicalAnalysisBinding.analysisName.text.toString()
                mAnalytics.analysis_price= mAddMedicalAnalysisBinding.analysisPrice.text.toString()
                lifecycleScope.launchWhenResumed {
                    mAddMedicalAnalysisViewModel.addMedicalAnalysis(requireContext(),Constants
                        .KEY_COLLECTION_LABS,mAnalytics)
                    mAddMedicalAnalysisBinding.analysisName.text.clear()
                    mAddMedicalAnalysisBinding.analysisPrice.text.clear()
            }
            }
        }
    }


    private fun isValidAnalysisDetails(): Boolean {
        when {
            mAddMedicalAnalysisBinding.analysisName.text.toString().trim().isEmpty() -> {
                mAddMedicalAnalysisBinding.analysisName.error =
                    resources.getString(R.string.enter_analysisName)
                mAddMedicalAnalysisBinding.analysisName.requestFocus()
                return false
            }
            mAddMedicalAnalysisBinding.analysisPrice.text.toString().trim().isEmpty() -> {
                mAddMedicalAnalysisBinding.analysisPrice.error =
                    resources.getString(R.string.enter_analysisPrice)
                mAddMedicalAnalysisBinding.analysisPrice.requestFocus()
                return false
            }
            else -> {
                return true
            }

        }
    }



    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mAddMedicalAnalysisViewModel.viewModelScope.cancel()
    }


}