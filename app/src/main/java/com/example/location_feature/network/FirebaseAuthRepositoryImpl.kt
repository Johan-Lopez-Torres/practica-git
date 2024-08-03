package com.example.location_feature.network

import android.util.Log
import com.example.location_feature.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {

    override suspend fun login(correo: String, clave: String): Boolean {
        return try {
            var isSuccessful: Boolean = false
            firebaseAuth.signInWithEmailAndPassword(correo, clave)
                .addOnSuccessListener { isSuccessful = true }
                .addOnFailureListener { isSuccessful = false }
                .await()
            isSuccessful
        } catch (e: Exception) {
            Log.d("test", e.toString())
            false
        }
    }

    override suspend fun signUp(correo: String, clave: String): Boolean {
        return try {
            var isSuccessful: Boolean = false
            firebaseAuth.createUserWithEmailAndPassword(correo, clave)
                .addOnCompleteListener { isSuccessful = it.isSuccessful}
                .await()
            isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}