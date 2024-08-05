package com.example.location_feature.domain.usecase

import com.esaudev.firebaseyt.domain.repository.UserRepository
import com.example.location_feature.domain.model.Usuario
import com.example.location_feature.domain.repository.AuthRepository
import com.example.location_feature.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseSignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        val userUID = authRepository.signUp(email,password)
        if (userUID.isNotEmpty()) {

            userRepository.createUser(
                Usuario(
                email = email,
                id = userUID
            )
            )

            emit(Resource.Success(true))
        } else {
            emit(Resource.Error("Sign up error"))
        }
    }
}