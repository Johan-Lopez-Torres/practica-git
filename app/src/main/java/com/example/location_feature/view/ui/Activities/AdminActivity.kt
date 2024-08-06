package com.example.location_feature.view.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.location_feature.R

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_admin) // Asegúrate de que tienes este layout

        val navController = findNavController(R.id.nav_graph)

        // El resto de la configuración se maneja en AdminFragment
    }
}
