package com.amrmedhatandroid.tahliluk_laboratory.fragments
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.adapters.ReservationAnalyticsAdapter
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.databinding.DialogProgressBinding
import com.amrmedhatandroid.tahliluk_laboratory.databinding.FragmentReservationDetailsBinding
import com.amrmedhatandroid.tahliluk_laboratory.models.Reserve
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.ReservationsDetailsViewModel

import java.io.Serializable



class ReservationDetailsFragment : Fragment(), Serializable {
    private lateinit var mReservationDetailsBinding: FragmentReservationDetailsBinding
    private lateinit var mReservationDetailsViewModel:ReservationsDetailsViewModel
    private lateinit var bundle: Bundle
    private lateinit var reservation:Reserve
    private lateinit var mShared:PreferenceManager
    private lateinit var image:String
    private lateinit var labName:String
    private lateinit var adapter:ReservationAnalyticsAdapter
    private lateinit var resultLauncher:ActivityResultLauncher<Intent>
    private lateinit var bindingDialog: DialogProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mShared = PreferenceManager(requireContext())
        bundle=requireArguments()
        reservation = bundle.getParcelable<Reserve>(Constants.Reservation) as Reserve
        labName = mShared.getString(Constants.KEY_LAB_NAME)!!
        image = mShared.getString(Constants.KEY_IMAGE)!!
        mReservationDetailsViewModel = ViewModelProvider(this)[ReservationsDetailsViewModel::class.java]
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // initialize result data
            val data: Intent = it.data!!
            val uri: Uri = data.data!!
            SupportClass.showProgressBar(
                requireContext(),
                resources.getString(R.string.please_wait),
                bindingDialog.tvProgressText
            )
            uploadResult(uri)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        mReservationDetailsBinding = FragmentReservationDetailsBinding.inflate(layoutInflater)
        bindingDialog = DialogProgressBinding.inflate(layoutInflater)
        setReservationData()
        return mReservationDetailsBinding.root
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            ReservationDetailsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun setReservationData() {
        mReservationDetailsBinding.tvOrderLabName.text = labName
        mReservationDetailsBinding.tvOrderDate.text = reservation.orderDateTime
        mReservationDetailsBinding.tvCheckoutSubTotal.text = reservation.orderAnalyticsPrice
        mReservationDetailsBinding.tvCheckoutTotalAmount.text = reservation.orderTotalAmount
        mReservationDetailsBinding.tvConfirmAdditionalNote.text =
            reservation.orderAdditionalInformation
        mReservationDetailsBinding.tvConfirmAddress.text = reservation.orderAddress
        mReservationDetailsBinding.tvCheckoutShippingCharge.text = getString(R.string.visit_price)
        when (reservation.orderState) {
            getString(R.string.pending) -> mReservationDetailsBinding.tvOrderState.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext()
                    , R.color.error_color)
            )
            getString(R.string.inprogress) -> mReservationDetailsBinding.tvOrderState.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext()
                    , R.color.error)
            )
            getString(R.string.completed) -> mReservationDetailsBinding.tvOrderState.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.primary_text)
            )

        }

        mReservationDetailsBinding.btnShowResult.setOnClickListener {
            selectResult()
        }
        mReservationDetailsBinding.tvOrderState.text = reservation.orderState
        mReservationDetailsBinding.ivLabImage.setImageBitmap(getLabImage(image))
        adapter = ReservationAnalyticsAdapter(reservation.analyticsList!!)
        mReservationDetailsBinding.rvCartListItems.layoutManager =
            LinearLayoutManager(requireContext())
        mReservationDetailsBinding.rvCartListItems.adapter = adapter

        if (reservation.results!=null){
            mReservationDetailsBinding.btnShowResult.text=getString(R.string.result_uploaded)
            mReservationDetailsBinding.btnShowResult.isEnabled=false
        }
    }

    private fun getLabImage(encodedImage: String): Bitmap {
        val bytes: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun selectResult(){

        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)

    }
    private fun uploadResult(Uri:Uri){
       lifecycleScope.launchWhenResumed {
           mReservationDetailsViewModel.uploadResult(
               this@ReservationDetailsFragment
               ,Uri
               ,"reservation")
       }
        }

    fun imageUploadSuccess(uri: String) {


        val reservationMap = HashMap<String,Any>()
        reservationMap[Constants.RESERVATION_RESULT] = uri
        reservationMap[Constants.ORDER_STATE] =Constants.ORDER_STATE_COMPLETED

        lifecycleScope.launchWhenResumed {
            mReservationDetailsViewModel.updateReservation(this@ReservationDetailsFragment,
            reservation.orderId!!,
            reservationMap)
        }

    }

    fun reservationUpdated() {
        mReservationDetailsBinding.btnShowResult.text = getString(R.string.result_uploaded)
        mReservationDetailsBinding.tvOrderState.text = Constants.ORDER_STATE_COMPLETED
        mReservationDetailsBinding.btnShowResult.isEnabled=false
        SupportClass.hideDialog()
    }

}


