package com.example.location_feature

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    @SuppressLint("VisibleForTests")
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            sendGeofenceNotification(context)
        }
    }

    private fun sendGeofenceNotification(context: Context) {
        // Aquí puedes personalizar los datos del mensaje
        val notificationMessage = "You have entered a geofenced area!"

        // Crear un mensaje FCM
        val message = RemoteMessage.Builder("93451503607@fcm.googleapis.com")
            .setMessageId(System.currentTimeMillis().toString())
            .addData("title", "Geofence Alert")
            .addData("body", notificationMessage)
            .build()

        FirebaseMessaging.getInstance().send(message)
    }
}
