package com.example.location_feature.view.ui.Activities

import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.location_feature.R
import com.example.location_feature.view.ui.Fragments.crud_crear


class CrearCuentaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_crud_crear) // Usa el layout del fragment

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.crear_cuenta_CRUD, crud_crear())
                .commitNow()
        }
    }
}