package com.example.location_feature.domain.usecase

import com.esaudev.firebaseyt.domain.repository.UserRepository
import com.example.location_feature.domain.model.Usuario
import com.example.location_feature.domain.repository.AuthRepository
import com.example.location_feature.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(email: String, password: String): Flow<Resource<Usuario>> = flow {
        emit(Resource.Loading)
        val userUID = authRepository.login(email,password)
        if (userUID.isNotEmpty()) {

            val user = userRepository.getUser(id = userUID)

            emit(Resource.Success(user))
            emit(Resource.Finished)
        } else {
            emit(Resource.Error("Login error"))
            emit(Resource.Finished)
        }
    }
}