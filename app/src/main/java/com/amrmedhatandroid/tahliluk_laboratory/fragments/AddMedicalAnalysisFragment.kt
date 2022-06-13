package com.amrmedhatandroid.tahliluk_laboratory.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amrmedhatandroid.tahliluk_laboratory.R


class AddMedicalAnalysisFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_medical_analysis, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddMedicalAnalysisFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }
}