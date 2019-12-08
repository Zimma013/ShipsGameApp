package pl.edu.agh.kis.android.ships.components

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CustomFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        println("Message received")

        val data = remoteMessage.data

        println("data --------- $data")
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}