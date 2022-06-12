package com.amrmedhatandroid.tahliluk_laboratory.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import androidx.recyclerview.widget.RecyclerView
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ItemContainerReservationsBinding
import com.amrmedhatandroid.tahliluk_laboratory.listeners.ReservationListener
import com.amrmedhatandroid.tahliluk_laboratory.models.Reserve


class ReservationsAdapter : RecyclerView.Adapter<ReservationsAdapter.ReservationsViewHolder> {
    private  lateinit var reservationsList:ArrayList<Reserve>
    private lateinit var listener: ReservationListener

    constructor()
    constructor(reservationsList: ArrayList<Reserve>, listener: ReservationListener ){
        this.reservationsList = reservationsList
        this.listener = listener

    }


   inner class ReservationsViewHolder(private var binding : ItemContainerReservationsBinding)
       :RecyclerView.ViewHolder(
       binding.root) {
       fun setReservationData(reserve: Reserve) {
           binding.tvOrderprice.text = reserve.orderTotalAmount
           binding.tvOrderState.text = reserve.orderState
           binding.tvOrderAddress.text = reserve.orderAddress
           binding.tvOrderDate.text = reserve.orderDateTime

           when(reserve.orderState){
               "Pending"  -> binding.tvOrderState.setTextColor(Color. parseColor("#B00020"))
               "Accepted" -> binding.tvOrderState.setTextColor(Color. parseColor("#284693"))
               "Completed" -> binding.tvOrderState.setTextColor(Color. parseColor("#284693"))
           }

           binding.root.setOnClickListener {
               listener.onReservationClickListener(reserve)
           }

       }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationsViewHolder {
        val binding =
            ItemContainerReservationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationsViewHolder, position: Int) {
       holder.setReservationData(reservationsList[position])
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rv_animation))
    }

    override fun getItemCount(): Int {
        return reservationsList.size
    }
}