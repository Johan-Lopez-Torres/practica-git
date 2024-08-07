package com.example.location_feature.view.ui.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.location_feature.R
import com.example.location_feature.view.ui.Fragments.crud_crear
import com.example.location_feature.view.ui.Fragments.editar_usuario

class EditarUsuariosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_editar_usuario) // Usa el layout del fragment

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.edit_user_layout, editar_usuario())
                .commitNow()
        }
    }
}