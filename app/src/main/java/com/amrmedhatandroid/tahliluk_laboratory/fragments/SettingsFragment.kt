package com.amrmedhatandroid.tahliluk_laboratory.fragments

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.activities.SplashScreenActivity
import com.amrmedhatandroid.tahliluk_laboratory.databinding.FragmentSettingsBinding
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ItemChangeLanguageDialogBinding
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.SettingsFragmentViewModel
import kotlinx.coroutines.flow.collect

class SettingsFragment : Fragment() {
    private lateinit var mFragmentSettingsBinding: FragmentSettingsBinding
    private lateinit var mItemChangeLanguageDialogBinding: ItemChangeLanguageDialogBinding
    private lateinit var mSettingsFragmentViewModel: SettingsFragmentViewModel
    private lateinit var mAlert: AlertDialog
    private var mAppLanguage: String? = null
    private var mEnglishRadioButtonState: Boolean = false
    private var mArabicRadioButtonState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettingsFragmentViewModel = ViewModelProvider(this)[SettingsFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater)
        return mFragmentSettingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            getDatKModeState()
        }

        mItemChangeLanguageDialogBinding = ItemChangeLanguageDialogBinding.inflate(
            layoutInflater, mFragmentSettingsBinding.root, false
        )
        setListeners()
    }

    private fun setListeners() {
        mFragmentSettingsBinding.btnChangeLanguage.setOnClickListener {
            showDialog()
        }
        mFragmentSettingsBinding.swDarkMode.setOnClickListener {
            if (mFragmentSettingsBinding.swDarkMode.isChecked) {
                startNightMode()
            } else {
                startLightMode()
            }
        }

        mItemChangeLanguageDialogBinding.rbEnglish.setOnClickListener {
            setEnglishLanguage()
        }

        mItemChangeLanguageDialogBinding.rbArabic.setOnClickListener {
            setArabicLanguage()
        }

    }


    private fun showDialog() {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        if (mItemChangeLanguageDialogBinding.root.parent != null) {
            (mItemChangeLanguageDialogBinding.root.parent as ViewGroup).removeView(
                mItemChangeLanguageDialogBinding.root
            )
        }
        builder.setView(mItemChangeLanguageDialogBinding.root)
        mAlert = builder.create()

        lifecycleScope.launchWhenResumed {
            getAppLanguage()
        }
        when (mAppLanguage) {
            Constants.KEY_LANGUAGE_ENGLISH -> {
                mItemChangeLanguageDialogBinding.rbEnglish.isChecked = true
                mItemChangeLanguageDialogBinding.rbArabic.isChecked = false
                mEnglishRadioButtonState = true
                mArabicRadioButtonState = false
            }
            Constants.KEY_LANGUAGE_ARABIC -> {
                mItemChangeLanguageDialogBinding.rbEnglish.isChecked = false
                mItemChangeLanguageDialogBinding.rbArabic.isChecked = true
                mEnglishRadioButtonState = false
                mArabicRadioButtonState = true
            }
        }

        if (mAlert.window != null) {
            mAlert.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        mAlert.show()
    }

    private fun setEnglishLanguage() {
        if (mEnglishRadioButtonState && !mArabicRadioButtonState) {
            mAlert.dismiss()
        } else {
            SupportClass.setLocale(Constants.KEY_LANGUAGE_ENGLISH, resources)
            mEnglishRadioButtonState = true
            mArabicRadioButtonState = false
            saveAppNewLanguage(Constants.KEY_LANGUAGE_ENGLISH)
            SupportClass.startActivityWithFlags(
                requireContext(),
                SplashScreenActivity::class.java
            )
        }
    }

    private fun setArabicLanguage() {
        if (mArabicRadioButtonState && !mEnglishRadioButtonState) {
            mAlert.dismiss()
        } else {
            SupportClass.setLocale(Constants.KEY_LANGUAGE_ARABIC, resources)
            mEnglishRadioButtonState = false
            mArabicRadioButtonState = true
            saveAppNewLanguage(Constants.KEY_LANGUAGE_ARABIC)
            SupportClass.startActivityWithFlags(
                requireContext(),
                SplashScreenActivity::class.java
            )
        }
    }

    private suspend fun getAppLanguage() {
        mSettingsFragmentViewModel.getAppLanguage(requireContext()).collect { language ->
            mAppLanguage = language
        }
    }

    private suspend fun getDatKModeState() {
        mSettingsFragmentViewModel.getAppDarkModeState(requireContext()).collect { state ->
            if (state == Constants.KEY_DARK_MODE) {
                mFragmentSettingsBinding.swDarkMode.isChecked = true
            }
        }
    }

    private fun saveAppNewLanguage(newLanguage: String) {
        mSettingsFragmentViewModel.saveAppNewLanguage(requireContext(), newLanguage)
    }

    private fun startNightMode() {
        lifecycleScope.launchWhenResumed {
            mSettingsFragmentViewModel.saveAppNewDarkModeState(
                requireContext(),
                Constants.KEY_DARK_MODE
            )
        }
        mFragmentSettingsBinding.swDarkMode.isChecked = true
        SupportClass.startNightMode()
    }

    private fun startLightMode() {
        lifecycleScope.launchWhenResumed {
            mSettingsFragmentViewModel.saveAppNewDarkModeState(
                requireContext(),
                Constants.KEY_LIGHT_MODE
            )
        }
        mFragmentSettingsBinding.swDarkMode.isChecked = false
        SupportClass.startLightMode()
    }
}