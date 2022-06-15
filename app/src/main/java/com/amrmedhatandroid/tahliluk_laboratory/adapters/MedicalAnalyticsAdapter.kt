package com.amrmedhatandroid.tahliluk_laboratory.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ItemContainerMedicalAnalysisBinding
import com.amrmedhatandroid.tahliluk_laboratory.models.Analytics


class MedicalAnalyticsAdapter(
    private var analytics: ArrayList<Analytics>,


) :
    RecyclerView.Adapter<MedicalAnalyticsAdapter.LabViewHolder>() {


    inner class LabViewHolder(var binding: ItemContainerMedicalAnalysisBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setAnalyticData(analyses: Analytics) {
            binding.textAnalysisName.text = analyses.analysis_name
            binding.textAnalysisPrice.text = analyses.analysis_price



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabViewHolder {
        val binding =
            ItemContainerMedicalAnalysisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LabViewHolder, position: Int) {
        holder.setAnalyticData(analytics[position])
//        holder.itemView.startAnimation(
//            AnimationUtils.loadAnimation(
//                holder.itemView.context,
//                R.anim.rv_animation
//            )
//        )
    }

    override fun getItemCount(): Int {
        return analytics.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNewList(newList: ArrayList<Analytics>) {
        this.analytics = newList
        notifyDataSetChanged()
    }



}