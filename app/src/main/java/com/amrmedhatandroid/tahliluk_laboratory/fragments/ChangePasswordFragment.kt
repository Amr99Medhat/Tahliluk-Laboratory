package com.amrmedhatandroid.tahliluk_laboratory.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.FragmentChangePasswordBinding
import com.amrmedhatandroid.tahliluk_laboratory.listeners.OnChangePasswordFragmentReturnListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChangePasswordFragment : BottomSheetDialogFragment() {
    private lateinit var mFragmentChangePasswordBinding: FragmentChangePasswordBinding
    private lateinit var mChangePasswordListener: OnChangePasswordFragmentReturnListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentChangePasswordBinding = FragmentChangePasswordBinding.inflate(inflater)
        return mFragmentChangePasswordBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        return super.onCreateDialog(savedInstanceState)
    }

    companion object {


        @JvmStatic
        fun newInstance(listenerChangePassword: OnChangePasswordFragmentReturnListener) =
            ChangePasswordFragment().apply {
                this.mChangePasswordListener = listenerChangePassword
            }
    }
}