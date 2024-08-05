package com.example.location_feature.network

import com.example.location_feature.domain.repository.UserRepository
import com.example.location_feature.domain.model.Usuario
import com.example.location_feature.network.FirebaseConstants.USERS_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserRepositoryImpl @Inject constructor(

): UserRepository {

    override suspend fun createUser(user: Usuario): Boolean {
        return try {
            var isSuccessful = false
            FirebaseFirestore.getInstance().collection(USERS_COLLECTION)
                .document(user.id)
                .set(user, SetOptions.merge())
                .addOnCompleteListener { isSuccessful = it.isSuccessful }
                .await()
            isSuccessful
        } catch (e: Exception) {
           false
        }
    }

    override suspend fun getUser(id: String): Usuario {
        return try {
            var loggedUser = Usuario()
            FirebaseFirestore.getInstance().collection(USERS_COLLECTION)
                .document(id)
                .get()
                .addOnSuccessListener {
                    loggedUser = it.toObject(Usuario::class.java)!!
                }
                .await()
            loggedUser
        } catch (e: Exception) {
            Usuario()
        }
    }
}