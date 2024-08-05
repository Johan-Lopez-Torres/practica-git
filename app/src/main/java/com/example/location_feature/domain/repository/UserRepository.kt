package com.example.location_feature.domain.repository

import com.example.location_feature.domain.model.Usuario

interface UserRepository {

    suspend fun createUser(user: Usuario): Boolean

    suspend fun getUser(id: String): Usuario
}