package com.amrmedhatandroid.tahliluk_laboratory.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ItemContainerRecentConversionBinding
import com.amrmedhatandroid.tahliluk_laboratory.listeners.ConversionListener
import com.amrmedhatandroid.tahliluk_laboratory.models.ChatMessage
import com.amrmedhatandroid.tahliluk_laboratory.models.Patient

class RecentConversationsAdapter(
    private var chatMessages: ArrayList<ChatMessage>,
    private var conversionListener: ConversionListener
) : RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder>() {

    inner class ConversionViewHolder(private var itemContainerRecentConversionBinding: ItemContainerRecentConversionBinding) :
        RecyclerView.ViewHolder(itemContainerRecentConversionBinding.root) {
        fun setData(chatMessage: ChatMessage) {
            itemContainerRecentConversionBinding.imageProfile.setImageBitmap(
                getConversionImage(
                    chatMessage.conversionImage!!
                )
            )
            itemContainerRecentConversionBinding.textPatientName.text = chatMessage.conversionName
            itemContainerRecentConversionBinding.textRecentMessage.text = chatMessage.message
            itemContainerRecentConversionBinding.root.setOnClickListener {
                val patient = Patient()
                patient.id = chatMessage.conversionId!!
                patient.firstName = chatMessage.conversionName!!
                patient.image = chatMessage.conversionImage!!
                conversionListener.onConversionClicked(patient)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversionViewHolder {
        return ConversionViewHolder(
            ItemContainerRecentConversionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ConversionViewHolder, position: Int) {
        holder.setData(chatMessages[position])
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    private fun getConversionImage(encodedImage: String): Bitmap {
        val bytes: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}