package com.amrmedhatandroid.tahliluk_laboratory.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.activities.MainChatActivity
import com.amrmedhatandroid.tahliluk_laboratory.activities.MedicalAnalysisActivity
import com.amrmedhatandroid.tahliluk_laboratory.activities.ReservationsActivity
import com.amrmedhatandroid.tahliluk_laboratory.databinding.FragmentHomeBinding
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.HomeFragmentViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment() {
    private lateinit var mFragmentHomeBinding: FragmentHomeBinding
    private lateinit var mHomeFragmentViewModel: HomeFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeFragmentViewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater)
        return mFragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadLabDetails()
        setListeners()
    }


    private fun loadLabDetails() {
        lifecycleScope.launchWhenResumed {
            mHomeFragmentViewModel.getLabName(requireContext()).collect {
                mFragmentHomeBinding.textWelcomeLab.text = it
            }
        }
        lifecycleScope.launchWhenResumed {
            mHomeFragmentViewModel.getLabImage(requireContext()).collect {
                val bitmap = SupportClass.getBitmapFromBytes(it)
                mFragmentHomeBinding.imageProfile.setImageBitmap(bitmap)
            }
        }
    }

    private fun setListeners() {
        mFragmentHomeBinding.cvReservations.setOnClickListener {
            startReservationsActivity()
        }
        mFragmentHomeBinding.cvChat.setOnClickListener {
            startMainChatActivity()
        }
        mFragmentHomeBinding.cvMedicalAnalysis.setOnClickListener {
            startMedicalAnalysisActivity()
        }
    }

    private fun startReservationsActivity() {
        if (SupportClass.checkForInternet(requireContext())) {
            SupportClass.startActivity(requireContext(), ReservationsActivity::class.java)
        } else {
            SupportClass.showNoInternetSnackBar(mFragmentHomeBinding)
        }
    }

    private fun startMainChatActivity() {
        if (SupportClass.checkForInternet(requireContext())) {
            SupportClass.startActivity(requireContext(), MainChatActivity::class.java)
        } else {
            SupportClass.showNoInternetSnackBar(mFragmentHomeBinding)
        }
    }

    private fun startMedicalAnalysisActivity() {
        SupportClass.startActivity(requireContext(), MedicalAnalysisActivity::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mHomeFragmentViewModel.viewModelScope.cancel()
    }
}