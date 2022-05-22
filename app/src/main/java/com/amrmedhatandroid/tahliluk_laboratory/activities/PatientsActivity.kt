package com.amrmedhatandroid.tahliluk_laboratory.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.adapters.PatientsAdapter
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ActivityPatientsBinding
import com.amrmedhatandroid.tahliluk_laboratory.listeners.PatientListener
import com.amrmedhatandroid.tahliluk_laboratory.models.Patient
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.PatientsViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect

class PatientsActivity : BaseActivity(), PatientListener {
    private lateinit var mActivityPatientsBinding: ActivityPatientsBinding
    private lateinit var mPatientsViewModel: PatientsViewModel
    private lateinit var mPatientsList: ArrayList<Patient>
    private lateinit var mPatientsListAdapter: PatientsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityPatientsBinding = ActivityPatientsBinding.inflate(layoutInflater)
        setContentView(mActivityPatientsBinding.root)
        mPatientsViewModel = ViewModelProvider(this)[PatientsViewModel::class.java]
        getPatients()
        setListeners()
    }

    private fun setListeners() {
        mActivityPatientsBinding.imageBack.setOnClickListener {
            onBackPressed()
        }
        mActivityPatientsBinding.search.setOnSearchClickListener {
            mActivityPatientsBinding.title.visibility = View.GONE
        }
    }

    private fun getPatients() {
        lifecycleScope.launchWhenResumed {
            mPatientsViewModel.getPatients().collect {
                mActivityPatientsBinding.textErrorMessage.visibility = View.GONE
                if (it.size > 0) {
                    mPatientsList = ArrayList()
                    mPatientsList = it
                    mActivityPatientsBinding.patientsRecyclerView.visibility = View.VISIBLE
                    SupportClass.loading(false, null, mActivityPatientsBinding.progressBar)
                    mPatientsListAdapter = PatientsAdapter(mPatientsList, this@PatientsActivity)
                    mActivityPatientsBinding.patientsRecyclerView.adapter =
                        mPatientsListAdapter
                }
            }
        }
    }

    override fun onPatientClicked(patient: Patient) {
        val intent = Intent(applicationContext, ChatActivity::class.java)
        intent.putExtra(Constants.KEY_PATIENT, patient)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mPatientsViewModel.viewModelScope.cancel()
    }

}