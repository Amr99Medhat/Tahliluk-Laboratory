package com.amrmedhatandroid.tahliluk_laboratory.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.FragmentChangePhoneBinding
import com.amrmedhatandroid.tahliluk_laboratory.listeners.OnChangePhoneFragmentReturnListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChangePhoneFragment : BottomSheetDialogFragment() {
    private lateinit var mFragmentChangePhoneBinding: FragmentChangePhoneBinding
    private lateinit var mChangePhoneListener: OnChangePhoneFragmentReturnListener
    private var mVerificationCodeBySystem: String? = null
    private var mNewNumber: String? = null
    private val mPatients = HashMap<String, Any>()
    private lateinit var mListenerChangePhone: OnChangePhoneFragmentReturnListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentChangePhoneBinding = FragmentChangePhoneBinding.inflate(inflater)
        return mFragmentChangePhoneBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(listenerChangePhone: OnChangePhoneFragmentReturnListener) =
            ChangePhoneFragment().apply {
                this.mListenerChangePhone = listenerChangePhone
            }
    }

}