package com.amrmedhatandroid.tahliluk_laboratory.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.activities.ChatActivity
import com.amrmedhatandroid.tahliluk_laboratory.models.Patient
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val patient = Patient()
        patient.id = remoteMessage.data["id"].toString()
        patient.firstName = remoteMessage.data["name"].toString()
        patient.fcmToken = remoteMessage.data["fcmToken"].toString()

        val notificationId: Int = Random().nextInt()
        val channelId = "chat_message"

        val intent = Intent(this, ChatActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(Constants.KEY_PATIENT, patient)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.notification_logo)
        builder.setContentTitle(patient.firstName)
        builder.setContentText(remoteMessage.data[Constants.KEY_MESSAGE])
        builder.setStyle(
            NotificationCompat.BigTextStyle().bigText(
                remoteMessage.data[Constants.KEY_MESSAGE]
            )
        )

        builder.priority = NotificationCompat.PRIORITY_MAX
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName: CharSequence = "Chat Message"
            val channelDescription =
                "This notification channel is used for chat message notification"
            val importance: Int = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)

            channel.description = channelDescription
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationManagerCompat: NotificationManagerCompat =
            NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(notificationId, builder.build())
    }

}