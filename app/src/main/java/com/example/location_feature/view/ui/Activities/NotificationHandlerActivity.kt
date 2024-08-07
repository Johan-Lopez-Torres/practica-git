package com.example.location_feature.view.ui.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import com.example.location_feature.R

class NotificationHandlerActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.mainFragmentContainer)

        handleIntent()
    }

    private fun handleIntent() {
        // Handle the navigation based on intent extras if needed
        // Example: Navigate to a specific fragment
        navController.navigate(R.id.action_to_home_mapa_to_home_mapa)
    }
}
