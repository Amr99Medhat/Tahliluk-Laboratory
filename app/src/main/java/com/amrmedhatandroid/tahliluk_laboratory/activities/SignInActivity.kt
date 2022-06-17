package com.amrmedhatandroid.tahliluk_laboratory.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ActivitySignInBinding
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ItemVerifiedDialogBinding
import com.amrmedhatandroid.tahliluk_laboratory.models.Lab
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.SignInViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect

class SignInActivity : AppCompatActivity() {
    private lateinit var mActivitySignInBinding: ActivitySignInBinding
    private lateinit var mItemVerifiedDialogLayoutBinding: ItemVerifiedDialogBinding
    private lateinit var mSignInViewModel: SignInViewModel
    private var mLabBasicData: Lab = Lab()
    private lateinit var mAlert: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivitySignInBinding = ActivitySignInBinding.inflate(layoutInflater)
        mItemVerifiedDialogLayoutBinding = ItemVerifiedDialogBinding.inflate(
            layoutInflater, mActivitySignInBinding.root, false
        )
        setContentView(mActivitySignInBinding.root)
        mSignInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]

        setListeners()
    }

    private fun setListeners() {
        mActivitySignInBinding.textSignUp.setOnClickListener {
            SupportClass.startActivity(this, SignUpActivity::class.java)
        }
        mActivitySignInBinding.btnSignIn.setOnClickListener {
            if (isValidSignInDetails()) {
                SupportClass.loading(
                    true,
                    mActivitySignInBinding.btnSignIn,
                    mActivitySignInBinding.progressBar
                )
                lifecycleScope.launchWhenResumed {
                    signIn()
                }
            }
        }
        mItemVerifiedDialogLayoutBinding.btnCancel.setOnClickListener {
            mAlert.dismiss()
        }
    }

    private fun isValidSignInDetails(): Boolean {
        when {
            mActivitySignInBinding.labPhoneNumber.text.toString().trim().isEmpty() -> {
                mActivitySignInBinding.labPhoneNumber.error =
                    resources.getString(R.string.enter_phone_number)
                mActivitySignInBinding.labPhoneNumber.requestFocus()
                return false
            }
            mActivitySignInBinding.labPhoneNumber.text.toString().trim().length != 11 -> {
                mActivitySignInBinding.labPhoneNumber.error =
                    resources.getString(R.string.must_be_11_digits)
                mActivitySignInBinding.labPhoneNumber.requestFocus()
                return false
            }
            mActivitySignInBinding.labPassword.text.toString().trim().isEmpty() -> {
                mActivitySignInBinding.labPassword.error =
                    resources.getString(R.string.enter_password)
                mActivitySignInBinding.labPassword.requestFocus()
                return false
            }
            mActivitySignInBinding.labPassword.text.toString().trim().length < 7 -> {
                mActivitySignInBinding.labPassword.error =
                    resources.getString(R.string.less_than_7_digits)
                mActivitySignInBinding.labPassword.requestFocus()
                return false
            }
            else -> {
                return true
            }
        }
    }

    private suspend fun signIn() {
        mSignInViewModel.signIn(
            mActivitySignInBinding.labPhoneNumber.text.toString(),
            mActivitySignInBinding.labPassword.text.toString()
        ).collect {
            if (it.id != Constants.KEY_EMPTY && it.id != Constants.KEY_STRING_MINUS_ONE) {
                if (it.labVerifiedState == Constants.KEY_LAB_VERIFIED) {
                    mLabBasicData = it

                    val labData = Lab()
                    labData.id = it.id
                    labData.image = it.image
                    labData.labName = it.labName
                    labData.phoneNumber = it.phoneNumber
                    labData.password = it.password
                    labData.latitude = it.latitude
                    labData.longitude = it.longitude

                    mSignInViewModel.saveBasicData(
                        this,
                        labData
                    )
                    startMainActivityWithFlags()
                } else {
                    SupportClass.loading(
                        false,
                        mActivitySignInBinding.btnSignIn,
                        mActivitySignInBinding.progressBar
                    )
                    showDialog()
                }

            } else if (it.id == Constants.KEY_STRING_MINUS_ONE) {
                unsuccessfulSignInMessage()
            }
        }
    }

    private fun unsuccessfulSignInMessage() {
        SupportClass.loading(
            false,
            mActivitySignInBinding.btnSignIn,
            mActivitySignInBinding.progressBar
        )
        SupportClass.showToast(
            applicationContext,
            resources.getString(R.string.unable_to_sign_in)
        )
    }

    private fun startMainActivityWithFlags() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        if (mItemVerifiedDialogLayoutBinding.root.parent != null) {
            (mItemVerifiedDialogLayoutBinding.root.parent as ViewGroup).removeView(
                mItemVerifiedDialogLayoutBinding.root
            )
        }
        builder.setView(mItemVerifiedDialogLayoutBinding.root)
        mAlert = builder.create()


        if (mAlert.window != null) {
            mAlert.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        mAlert.show()

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mSignInViewModel.viewModelScope.cancel()
    }
}