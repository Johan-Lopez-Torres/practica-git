package com.example.location_feature.view.ui.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.location_feature.R
import com.example.location_feature.domain.model.Comentario
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class ComentarioActivity : AppCompatActivity() {

    private lateinit var temaEditText: EditText
    private lateinit var comentarioEditText: EditText
    private lateinit var enviarButton: Button
    private lateinit var regresarButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_comentarios_cuenta)

        temaEditText = findViewById(R.id.tv_texto_commentario)
        comentarioEditText = findViewById(R.id.tv_texto_commentario2)
        enviarButton = findViewById(R.id.btnEnvia_comentarior)
        regresarButton = findViewById(R.id.btRegreo2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.comentarios)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle button click to send data
        enviarButton.setOnClickListener {
            val tema = temaEditText.text.toString()
            val comentario = comentarioEditText.text.toString()

            if (tema.isNotBlank() && comentario.isNotBlank()) {
                val comentarioId = generateComentarioId()
                sendComentarioToFirebase(comentarioId, tema, comentario)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle button click to go back
        regresarButton.setOnClickListener {
            Toast.makeText(this, "de regreso a la pantalla principal", Toast.LENGTH_SHORT).show()
        }
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
                Toast.makeText(this, "Comentario enviado con éxito", Toast.LENGTH_SHORT).show()
                temaEditText.text.clear()
                comentarioEditText.text.clear()
            }

    }
}