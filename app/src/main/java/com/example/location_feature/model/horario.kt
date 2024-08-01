package com.example.location_feature.model
data class Horario (
    val id : String ,
    val horaInicio: String,
    val horaFin: String,
    var idRuta: String,
    var idZona: String,
    var dias: String
)