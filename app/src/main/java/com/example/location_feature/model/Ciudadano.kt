package com.example.location_feature.model

import android.os.Parcel
import android.os.Parcelable

import java.util.Date

data class Ciudadano(
    var id: String = "",
    var nombres: String = "",
    var apellidos: String = "",
    var correo: String = "",
    var clave: String = "",
    var creado_en: Date = Date(),
    var actualizado_en: Date = Date()
)