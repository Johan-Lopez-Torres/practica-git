package com.example.ecoferia.network


import com.example.location_feature.domain.model.Usuarios
import com.example.location_feature.network.Callback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

const val USUARIOS_COLLECTION_NAME = "usuarios"

class FirestoreService {
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

    init {
        firebaseFirestore.firestoreSettings = settings
    }

    fun crearUsuario(usuario: Usuarios, callback: Callback<Boolean>) {
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

    fun getUsuarios(callback: (List<Usuarios>) -> Unit) {
        firebaseFirestore.collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                val usuarios = mutableListOf<Usuarios>()
                for (document in result) {
                    val usuario = document.toObject(Usuarios::class.java)
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


//    fun obtenerUsuarios(callback: (List<Usuarios>) -> Unit) {
//        firebaseFirestore.collection(USUARIOS_COLLECTION_NAME)
//            .get()
//            .addOnSuccessListener { result ->
//                val usuarios = mutableListOf<Usuarios>()
//                for (document in result) {
//                    val usuario = document.toObject(Usuarios::class.java)
//                    usuarios.add(usuario)
//                }
//                callback(usuarios) // Llama al callback con la lista de usuarios
//            }
//            .addOnFailureListener { exception ->
//                // Manejo de errores si es necesario
//                callback(emptyList()) // Llama al callback con una lista vacía en caso de error
//            }
//    }
}
