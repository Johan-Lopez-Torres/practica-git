package com.example.location_feature.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.location_feature.R
import com.example.location_feature.model.Usuarios
import android.widget.TextView

class UsuariosAdapter(private val usuariosList: List<Usuarios>, private val onItemClick: (Usuarios) -> Unit) :
    RecyclerView.Adapter<UsuariosAdapter.UsuarioViewHolder>() {

    private var selectedUser: Usuarios? = null // Variable para almacenar el usuario seleccionado

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leer_user, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuariosList[position]
        holder.bind(usuario)

        // Cambiar el fondo del elemento seleccionado
        holder.itemView.setBackgroundColor(
            if (usuario == selectedUser) Color.LTGRAY else Color.WHITE
        )

        holder.itemView.setOnClickListener {
            selectedUser = usuario // Actualiza el usuario seleccionado
            notifyDataSetChanged() // Notifica a todos los elementos para que se actualicen
            onItemClick(usuario) // Llama a la función de clic
        }
    }

    override fun getItemCount(): Int = usuariosList.size

    fun getSelectedUser(): Usuarios? {
        return selectedUser // Devuelve el usuario seleccionado
    }

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userCorreo: TextView = itemView.findViewById(R.id.text_view_user_correo)
        private val userClave: TextView = itemView.findViewById(R.id.text_view_user_last_clave)
        private val userRol: TextView = itemView.findViewById(R.id.text_view_user_rol)

        fun bind(usuario: Usuarios) {
            userCorreo.text = usuario.email
            userClave.text = usuario.password
            userRol.text = usuario.role
        }
    }
}