package com.amrmedhatandroid.tahliluk_laboratory.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ItemContainerPatientBinding
import com.amrmedhatandroid.tahliluk_laboratory.listeners.PatientListener
import com.amrmedhatandroid.tahliluk_laboratory.models.Patient

class PatientsAdapter :
    RecyclerView.Adapter<PatientsAdapter.PatientViewHolder> {
    private var mPatients: ArrayList<Patient> = ArrayList()
    private lateinit var mPatientListener: PatientListener

    constructor()
    constructor(labs: ArrayList<Patient>, patientListener: PatientListener) {
        this.mPatients = labs
        this.mPatientListener = patientListener
    }

    inner class PatientViewHolder(var itemContainerPatientBinding: ItemContainerPatientBinding) :
        RecyclerView.ViewHolder(itemContainerPatientBinding.root) {
        @SuppressLint("SetTextI18n")
        fun setLabData(Patient: Patient) {
            itemContainerPatientBinding.patientName.text =
                "${Patient.firstName} ${Patient.lastName} "
            itemContainerPatientBinding.patientImageProfile.setImageBitmap(getPatientImage(Patient.image))
            itemContainerPatientBinding.root.setOnClickListener {
                mPatientListener.onPatientClicked(Patient)
            }
        }
    }

    private fun getPatientImage(encodedImage: String): Bitmap {
        val bytes: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding =
            ItemContainerPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.setLabData(mPatients[position])
    }

    override fun getItemCount(): Int {
        return mPatients.size
    }
}