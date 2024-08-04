package com.example.location_feature.network

import android.util.Log
import com.example.location_feature.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Login error: ${e.localizedMessage}", e)
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .await()
            true
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "SignUp error: ${e.message}", e)
            false
        }
    }
}