package com.example.location_feature.domain.usecase

import com.example.location_feature.domain.repository.AuthRepository
import com.example.location_feature.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseSignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        try {
            val isSignUpSuccessfully = authRepository.signUp(email, password)
            if (isSignUpSuccessfully) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error("Sign up error"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unexpected error occurred"))
        } finally {
            emit(Resource.Finished)
        }
    }
}