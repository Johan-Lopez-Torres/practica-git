package com.example.location_feature.usercase

import com.example.location_feature.repository.AuthRepository
import com.example.location_feature.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseLoginCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(correo: String, clave: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        val loggedSuccessfully = authRepository.login(correo,clave)
        if (loggedSuccessfully) {
            emit(Resource.Success(true))
            emit(Resource.Finished)
        } else {
            emit(Resource.Error("Login error"))
            emit(Resource.Finished)
        }
    }
}