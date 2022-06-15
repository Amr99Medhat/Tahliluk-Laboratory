package com.amrmedhatandroid.tahliluk_laboratory.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ItemContainerReceivedMessageBinding
import com.amrmedhatandroid.tahliluk_laboratory.databinding.ItemContainerSentMessageBinding
import com.amrmedhatandroid.tahliluk_laboratory.models.ChatMessage

class ChatAdapter(
    private var senderId: String,
    private var chatMessages: ArrayList<ChatMessage>,
    private var receiverProfileImage: Bitmap?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    inner class SentMessageViewHolder(private var itemContainerSentMessageBinding: ItemContainerSentMessageBinding) :
        RecyclerView.ViewHolder(itemContainerSentMessageBinding.root) {
        fun setData(chatMessage: ChatMessage) {
            itemContainerSentMessageBinding.textMessage.text = chatMessage.message
            itemContainerSentMessageBinding.textDateTime.text = chatMessage.dateTime
        }
    }

    inner class ReceivedMessageViewHolder(private var itemContainerReceivedMessageBinding: ItemContainerReceivedMessageBinding) :
        RecyclerView.ViewHolder(itemContainerReceivedMessageBinding.root) {
        fun setData(chatMessage: ChatMessage, receiverProfileImage: Bitmap?) {
            itemContainerReceivedMessageBinding.textMessage.text = chatMessage.message
            itemContainerReceivedMessageBinding.textDateTime.text = chatMessage.dateTime
            if (receiverProfileImage != null) {
                itemContainerReceivedMessageBinding.imageProfile.setImageBitmap(receiverProfileImage)
            }
        }
    }

//    fun setReceiverImageProfile(bitmap: Bitmap) {
//        receiverProfileImage = bitmap
//    }

    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].senderId.equals(senderId)) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_SENT) {
            return SentMessageViewHolder(
                ItemContainerSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return ReceivedMessageViewHolder(
                ItemContainerReceivedMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            (holder as SentMessageViewHolder).setData(chatMessages[position])
            holder.itemView.startAnimation(
                AnimationUtils.loadAnimation(
                    holder.itemView.context,
                    R.anim.rv_animation_sent
                )
            )
        } else {
            (holder as ReceivedMessageViewHolder).setData(
                chatMessages[position],
                receiverProfileImage!!
            )
            holder.itemView.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rv_animation_recived))
        }
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

}