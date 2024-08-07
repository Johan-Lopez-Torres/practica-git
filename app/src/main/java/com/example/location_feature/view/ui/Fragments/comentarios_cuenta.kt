package com.example.location_feature.view.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.location_feature.R
import com.example.location_feature.domain.model.Comentario
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class comentarios_cuenta : Fragment() {

    private lateinit var temaEditText: EditText
    private lateinit var comentarioEditText: EditText
    private lateinit var enviarButton: Button
    private lateinit var regresarButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comentarios_cuenta, container, false)

        temaEditText = view.findViewById(R.id.tv_texto_commentario)
        comentarioEditText = view.findViewById(R.id.tv_texto_commentario2)
        enviarButton = view.findViewById(R.id.btnEnvia_comentarior)
        regresarButton = view.findViewById(R.id.btRegreo2)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.comentarios)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        enviarButton.setOnClickListener {
            val tema = temaEditText.text.toString()
            val comentario = comentarioEditText.text.toString()

            if (tema.isNotBlank() && comentario.isNotBlank()) {
                val comentarioId = generateComentarioId()
                sendComentarioToFirebase(comentarioId, tema, comentario)
            } else {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        regresarButton.setOnClickListener {
            // Handle navigation back to the previous fragment
            activity?.onBackPressed()
        }

        return view
    }

    private fun generateComentarioId(): String {
        val random = Random(System.currentTimeMillis())
        val number = random.nextInt(99) + 1
        return "comentario$number"
    }

    private fun sendComentarioToFirebase(id: String, tema: String, comentario: String) {
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val comentarioObject = Comentario(id, tema, comentario)

        firebaseFirestore.collection("Comentarios")
            .add(comentarioObject)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Comentario enviado con éxito", Toast.LENGTH_SHORT).show()
                temaEditText.text.clear()
                comentarioEditText.text.clear()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al enviar el comentario", Toast.LENGTH_SHORT).show()
            }
    }
}