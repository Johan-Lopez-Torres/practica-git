package com.example.location_feature.network.dependencyInjection
import com.example.location_feature.network.FirebaseAuthRepositoryImpl

import com.example.location_feature.domain.repository.AuthRepository
import com.example.location_feature.domain.repository.UserRepository
import com.example.location_feature.network.FirestoreUserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(authRepository: FirebaseAuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindUserRepository(userRepository: FirestoreUserRepositoryImpl): UserRepository
}