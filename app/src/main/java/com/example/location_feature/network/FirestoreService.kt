package com.example.ecoferia.network


import com.example.location_feature.domain.model.Usuario
import com.example.location_feature.network.Callback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

const val USUARIOS_COLLECTION_NAME = "Usuarios"

class FirestoreService {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

    init {
        firebaseFirestore.firestoreSettings = settings
    }

    fun crearUsuario(usuario: Usuario, callback: Callback<Boolean>) {
        firebaseFirestore.collection(USUARIOS_COLLECTION_NAME)
            .add(usuario)
            .addOnSuccessListener {
                callback.onSuccess(true) // Indica éxito
            }
            .addOnFailureListener { e ->
                callback.onError(e) // Maneja el error
                callback.onSuccess(false) // Indica fallo
            }
    }

    fun getUsuarios(callback: (List<Usuario>) -> Unit) {
        firebaseFirestore.collection("Usuarios")
            .get()
            .addOnSuccessListener { result ->
                val usuarios = mutableListOf<Usuario>()
                for (document in result) {
                    val usuario = document.toObject(Usuario::class.java)
                    usuario.id = document.id // Asigna el ID del documento a la propiedad id del usuario
                    usuarios.add(usuario)
                }
                callback(usuarios)
            }
            .addOnFailureListener { exception ->
                // Manejo de errores
            }
    }

    fun eliminarUsuario(usuarioId: String, callback: (Boolean) -> Unit) {
        val docRef = firebaseFirestore.collection(USUARIOS_COLLECTION_NAME).document(usuarioId)
        docRef.delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }


    fun actualizarUsuario(usuario: Usuario, callback: Callback<Boolean>) {
        val docRef = firebaseFirestore.collection(USUARIOS_COLLECTION_NAME).document(usuario.id)
        docRef.set(usuario)
            .addOnSuccessListener { callback.onSuccess(true) }
            .addOnFailureListener { e ->
                callback.onError(e)
                callback.onSuccess(false)
            }
    }
}
