package com.amrmedhatandroid.tahliluk_laboratory.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ActivitySignUpBinding
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.SignUpViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import java.io.FileNotFoundException
import java.io.InputStream

class SignUpActivity : AppCompatActivity() {
    private lateinit var mActivitySignUpBinding: ActivitySignUpBinding
    private lateinit var mSignUpViewModel: SignUpViewModel
    private var mCurrentLatLong: LatLng? = null
    private var mEncodedImage: String? = null
    private var mAddress:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mActivitySignUpBinding.root)
        mSignUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        setListeners()
    }

    private fun setListeners() {
        mActivitySignUpBinding.textSignIn.setOnClickListener {
            onBackPressed()
        }
        mActivitySignUpBinding.btnSignUp.setOnClickListener {
            if (isValidSignUpDetails()) {
                SupportClass.loading(
                    true,
                    mActivitySignUpBinding.btnSignUp,
                    mActivitySignUpBinding.progressBar
                )
                lifecycleScope.launchWhenResumed {
                    checkIfExistInLabs()
                }
            }
        }
        mActivitySignUpBinding.labImage.setOnClickListener {
            mSignUpViewModel.pickImage(resultLauncher)
        }
        mActivitySignUpBinding.labLocation.setOnClickListener {
            mSignUpViewModel.getLocation(this, resultLauncher)
        }
    }

    private suspend fun checkIfExistInLabs() {
        mSignUpViewModel.checkIfExist(
            Constants.KEY_COLLECTION_LABS,
            Constants.KEY_LAB_PHONE_NUMBER,
            mActivitySignUpBinding.labPhoneNumber.text.toString()
        ).collect { patient ->
            when (patient) {
                Constants.KEY_TRUE_RETURN -> {
                    setNumberError()
                }
                Constants.KEY_FALSE_RETURN -> {
                    checkIfExistInPatients()
                }
            }
        }


    }

    private suspend fun checkIfExistInPatients() {
        mSignUpViewModel.checkIfExist(
            Constants.KEY_COLLECTION_PATIENTS,
            Constants.KEY_LAB_PHONE_NUMBER,
            mActivitySignUpBinding.labPhoneNumber.text.toString()
        ).collect { lab ->
            when (lab) {
                Constants.KEY_TRUE_RETURN -> {
                    setNumberError()
                }
                Constants.KEY_FALSE_RETURN -> {
                    signUp()
                }
            }
        }
    }

    private fun signUp() {
        SupportClass.loading(
            false,
            mActivitySignUpBinding.btnSignUp,
            mActivitySignUpBinding.progressBar
        )
        goForVerification()

    }

    private fun isValidSignUpDetails(): Boolean {
        if (mEncodedImage == null) {
            SupportClass.showToast(
                applicationContext,
                resources.getString(R.string.select_profile_image)
            )
            return false
        } else if (mActivitySignUpBinding.labName.text.toString().trim().isEmpty()) {
            mActivitySignUpBinding.labName.error = resources.getString(R.string.enter_lab_name)
            mActivitySignUpBinding.labName.requestFocus()
            return false
        } else if (mActivitySignUpBinding.labPhoneNumber.text.toString().trim().isEmpty()) {
            mActivitySignUpBinding.labPhoneNumber.error =
                resources.getString(R.string.enter_phone_number)
            mActivitySignUpBinding.labPhoneNumber.requestFocus()
            return false
        } else if (mActivitySignUpBinding.labPhoneNumber.text.toString().trim().length != 11) {
            mActivitySignUpBinding.labPhoneNumber.error =
                resources.getString(R.string.must_be_11_digits)
            mActivitySignUpBinding.labPhoneNumber.requestFocus()
            return false
        } else if (mActivitySignUpBinding.labPassword.text.toString().trim().isEmpty()) {
            mActivitySignUpBinding.labPassword.error = resources.getString(R.string.enter_password)
            mActivitySignUpBinding.labPassword.requestFocus()
            return false
        } else if (mActivitySignUpBinding.labPassword.text.toString().trim().length < 7) {
            mActivitySignUpBinding.labPassword.error =
                resources.getString(R.string.less_than_7_digits)
            mActivitySignUpBinding.labPassword.requestFocus()
            return false
        } else if (mActivitySignUpBinding.labConfirmPassword.text.toString().trim().isEmpty()) {
            mActivitySignUpBinding.labConfirmPassword.error =
                resources.getString(R.string.enter_confirm_password)
            mActivitySignUpBinding.labConfirmPassword.requestFocus()
            return false
        } else if (mActivitySignUpBinding.labPassword.text.toString() != mActivitySignUpBinding.labConfirmPassword.text.toString()) {
            mActivitySignUpBinding.labConfirmPassword.error =
                resources.getString(R.string.not_the_same_password)
            return false
        } else if (mActivitySignUpBinding.labLocation.text == resources.getString(R.string.location)) {
            mActivitySignUpBinding.labLocation.error =
                resources.getString(R.string.location_null_message)
            SupportClass.showToast(
                applicationContext,
                resources.getString(R.string.location_null_message)
            )
            return false
        } else {
            return true
        }
    }

    private fun setNumberError() {
        mActivitySignUpBinding.labPhoneNumber.error =
            resources.getString(R.string.this_phone_number_already_have_an_account)
        mActivitySignUpBinding.labPhoneNumber.requestFocus()
        SupportClass.loading(
            false,
            mActivitySignUpBinding.btnSignUp,
            mActivitySignUpBinding.progressBar
        )
    }

    private fun goForVerification() {
        val intent = Intent(this, VerifyPhoneNumberActivity::class.java)
        intent.putExtra(Constants.KEY_IMAGE, mEncodedImage!!)
        intent.putExtra(Constants.KEY_LAB_NAME, mActivitySignUpBinding.labName.text.toString())
        intent.putExtra(
            Constants.KEY_LAB_PHONE_NUMBER,
            mActivitySignUpBinding.labPhoneNumber.text.toString()
        )
        intent.putExtra(Constants.KEY_PASSWORD, mActivitySignUpBinding.labPassword.text.toString())
        intent.putExtra(Constants.KEY_LOCATION_RESULT, mCurrentLatLong)
        intent.putExtra(Constants.KEY_LUNCH_STATE, Constants.KEY_LUNCH_STATE_FIRST_TIME)
        intent.putExtra(Constants.KEY_LOCATION_Address_Result,mAddress)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }


    @SuppressLint("SetTextI18n")
    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data != null) {
                when (result.resultCode) {
                    RESULT_OK -> {
                        val imageUri: Uri? = result.data!!.data
                        try {
                            val inputStream: InputStream? =
                                contentResolver.openInputStream(imageUri!!)
                            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                            mActivitySignUpBinding.labImage.setImageBitmap(bitmap)
                            mEncodedImage = SupportClass.encodedImage(bitmap)

                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                    Constants.KEY_LOCATION_RESULT_CODE -> {
                        val intent = result.data
                        if (intent != null) {
                            val address =
                                intent.getStringExtra(Constants.KEY_LOCATION_Address_Result)
                            mAddress = address
                            val latLng =
                                intent.getParcelableExtra<LatLng>(Constants.KEY_LOCATION_RESULT)
                            mCurrentLatLong = latLng

                            mActivitySignUpBinding.labLocation.text =
                               address

                        }
                    }


                }
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
        mSignUpViewModel.viewModelScope.cancel()
    }

}