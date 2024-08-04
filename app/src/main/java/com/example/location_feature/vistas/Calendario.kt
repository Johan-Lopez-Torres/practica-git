package com.example.location_feature.vistas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import com.example.location_feature.R


class Calendario : Fragment() {
    private lateinit var calendarView: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_calendario, container, false)

        calendarView = view.findViewById(R.id.miCalendario) // Asegúrate de que el ID coincida con el del layout

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(activity, "Seleccionaste: $date", Toast.LENGTH_SHORT).show()
        }
        return view
    }

}