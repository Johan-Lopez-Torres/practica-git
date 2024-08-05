package com.example.location_feature

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.location_feature.view.ui.Activities.MapsActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent


class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG: String = "GeofenceBroadcastReceiv"

    @SuppressLint("VisibleForTests")
    override fun onReceive(context: Context?, intent: Intent?) {

        Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_SHORT).show();
        val notificationHelper = NotificationHelper(context)
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...")
            return
        }
        val geofenceList = geofencingEvent.triggeringGeofences
        for (geofence in geofenceList) {
            Log.d(TAG, "onReceive geodenceEvent: " + geofence.requestId)
        }
        //        Location location = geofencingEvent.getTriggeringLocation();
        val transitionType = geofencingEvent.geofenceTransition
        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification(
                    "GEOFENCE_TRANSITION_ENTER", "dfasdsadsdf",
                    MapsActivity::class.java
                )
            }

            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification(
                    "GEOFENCE_TRANSITION_DWELL", "",
                    MapsActivity::class.java
                )
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show()
                notificationHelper.sendHighPriorityNotification(
                    "GEOFENCE_TRANSITION_EXIT", "",
                    MapsActivity::class.java
                )
            }
        }
    }
}
