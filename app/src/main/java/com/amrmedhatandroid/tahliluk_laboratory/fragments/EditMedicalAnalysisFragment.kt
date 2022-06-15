package com.amrmedhatandroid.tahliluk_laboratory.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.FragmentEditMedicalAnalysisBinding
import com.amrmedhatandroid.tahliluk_laboratory.models.Analytics
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.EditMedicalAnalysisFragmentViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import java.util.ArrayList


class EditMedicalAnalysisFragment : Fragment() {
    private lateinit var mEditMedicalAnalysisBinding: FragmentEditMedicalAnalysisBinding
    private lateinit var mAnalysis:Analytics
    private var mPosition:Int?=null
    private lateinit var  medicalAnalytics:ArrayList<Analytics>
    private lateinit var mEditMedicalFragmentViewModel: EditMedicalAnalysisFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
          mAnalysis = it.getSerializable(Constants.EDIE_ANALYSIS) as Analytics
            mPosition = it.getInt(Constants.ANALYSIS_POSITION)

        }
        mEditMedicalFragmentViewModel = ViewModelProvider(this)[EditMedicalAnalysisFragmentViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mEditMedicalAnalysisBinding = FragmentEditMedicalAnalysisBinding.inflate(layoutInflater)
        getMedicalAnalysis()
        setData()
        editAnalysis()
        return mEditMedicalAnalysisBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            EditMedicalAnalysisFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    private fun getMedicalAnalysis(){
        lifecycleScope.launchWhenResumed {
            mEditMedicalFragmentViewModel.getMedicalAnalysis(requireContext(),Constants.KEY_COLLECTION_LABS).collect {
                medicalAnalytics = it
            }
        }
    }

    private fun setData(){
        mEditMedicalAnalysisBinding.analysisName.setText(mAnalysis.analysis_name)
        mEditMedicalAnalysisBinding.analysisPrice.setText(mAnalysis.analysis_price)

    }
    private fun editAnalysis()
    {
        mEditMedicalAnalysisBinding.btnEditAnalysis.setOnClickListener {
            mAnalysis.analysis_name = mEditMedicalAnalysisBinding.analysisName.text.toString()
            mAnalysis.analysis_price = mEditMedicalAnalysisBinding.analysisPrice.text.toString()
            medicalAnalytics[mPosition!!] = mAnalysis
            if (isValidAnalysisDetails()){
                mEditMedicalFragmentViewModel.editMedicalAnalysis(requireContext(),Constants.KEY_COLLECTION_LABS,medicalAnalytics)
                val medicalAnalysisFragment = MedicalAnalyticsFragment.newInstance()
                val fragmentManager: FragmentManager =
                    (mEditMedicalAnalysisBinding.root.context as FragmentActivity).supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(
                    R.anim.fui_slide_in_right,
                    R.anim.fragmentanimation,
                    R.anim.fui_slide_in_right,
                    R.anim.fragmentanimation
                )
                fragmentTransaction.remove(this)
                fragmentTransaction.replace(R.id.fragment_container, medicalAnalysisFragment)
                fragmentTransaction.commit()


            }
        }
    }

    private fun isValidAnalysisDetails(): Boolean {
        when {
            mEditMedicalAnalysisBinding.analysisName.text.toString().trim().isEmpty() -> {
                mEditMedicalAnalysisBinding.analysisName.error =
                    resources.getString(R.string.enter_analysisName)
                mEditMedicalAnalysisBinding.analysisName.requestFocus()
                return false
            }
            mEditMedicalAnalysisBinding.analysisPrice.text.toString().trim().isEmpty() -> {
                mEditMedicalAnalysisBinding.analysisPrice.error =
                    resources.getString(R.string.enter_analysisPrice)
                mEditMedicalAnalysisBinding.analysisPrice.requestFocus()
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
        mEditMedicalFragmentViewModel.viewModelScope.cancel()
    }

}