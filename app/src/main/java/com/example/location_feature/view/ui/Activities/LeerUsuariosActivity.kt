package com.example.location_feature.view.ui.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.location_feature.R

class LeerUsuariosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_leer_usuario) // Usa el layout del fragment

        val navController = findNavController(R.id.nav_graph)

    }
}