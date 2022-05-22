package com.amrmedhatandroid.tahliluk_laboratory.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.activities.SplashScreenActivity
import com.amrmedhatandroid.tahliluk_laboratory.databinding.FragmentProfileBinding
import com.amrmedhatandroid.tahliluk_laboratory.listeners.OnChangePasswordFragmentReturnListener
import com.amrmedhatandroid.tahliluk_laboratory.listeners.OnChangePhoneFragmentReturnListener
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.ProfileFragmentViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect

class ProfileFragment : Fragment(), OnChangePasswordFragmentReturnListener,
    OnChangePhoneFragmentReturnListener {
    private lateinit var mFragmentProfileBinding: FragmentProfileBinding
    private lateinit var mProfileFragmentViewModel: ProfileFragmentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProfileFragmentViewModel = ViewModelProvider(this)[ProfileFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentProfileBinding = FragmentProfileBinding.inflate(inflater)
        return mFragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPatientDetails()
        setListener()

    }

    private fun loadPatientDetails() {
        lifecycleScope.launchWhenResumed {
            mProfileFragmentViewModel.getLabImage(requireContext()).collect {
                val bitmap = SupportClass.getBitmapFromBytes(it)
                mFragmentProfileBinding.ivLabImage.setImageBitmap(bitmap)
            }
        }
        lifecycleScope.launchWhenResumed {
            mProfileFragmentViewModel.getLabPhoneNumber(requireContext()).collect {
                mFragmentProfileBinding.btnChangeLabPhoneNumber.text = it
            }
        }
    }

    private fun setListener() {
        mFragmentProfileBinding.tvChangePassword.setOnClickListener {
            ChangePasswordFragment.newInstance(this)
                .show(childFragmentManager, Constants.TAG_CHANGE_PASSWORD)
        }
        mFragmentProfileBinding.btnChangeLabPhoneNumber.setOnClickListener {
            ChangePhoneFragment.newInstance(this)
                .show(childFragmentManager, Constants.TAG_CHANGE_PHONE)
        }
        mFragmentProfileBinding.btnLogout.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                signOut()
            }
        }
    }

    private suspend fun signOut() {
        mProfileFragmentViewModel.signOut(requireContext()).collect { state ->
            when (state) {
                Constants.KEY_TRUE_RETURN -> {
                    SupportClass.startActivityWithFlags(
                        requireContext(),
                        SplashScreenActivity::class.java
                    )
                }
                Constants.KEY_FALSE_RETURN -> {
                    SupportClass.showToast(
                        requireContext(),
                        resources.getString(R.string.unable_to_sign_out)
                    )
                }
            }
        }
    }


    override fun onChangePasswordFragmentReturn(status: Boolean) {
        if (status) {
            lifecycleScope.launchWhenResumed {
                signOut()
            }
        }
    }

    override fun onChangePhoneFragmentReturn(newNumber: String) {
        mFragmentProfileBinding.btnChangeLabPhoneNumber.text = newNumber
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mProfileFragmentViewModel.viewModelScope.cancel()
    }
}