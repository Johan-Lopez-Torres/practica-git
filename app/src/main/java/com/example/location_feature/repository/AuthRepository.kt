package com.example.location_feature.repository

interface AuthRepository {

        suspend fun login(correo: String, clave:String): Boolean
        suspend fun signUp(correo:String, clave: String): Boolean
}