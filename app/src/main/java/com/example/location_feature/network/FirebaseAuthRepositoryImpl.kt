package com.example.location_feature.network

import android.util.Log
import com.example.location_feature.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {

    override suspend fun login(email: String, password: String): String {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.uid ?: ""
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Error al iniciar sesión", e)
            ""
        }
    }

    override suspend fun signUp(email: String, password: String): String {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userUID = result.user?.uid ?: ""

            if (userUID.isNotEmpty()) {
                val userData = mapOf("email" to email)
                FirebaseFirestore.getInstance().collection("Usuarios")
                    .document(userUID)
                    .set(userData)
                    .await()
            }

            userUID
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Error al crear cuenta", e)
            ""
        }
    }
}