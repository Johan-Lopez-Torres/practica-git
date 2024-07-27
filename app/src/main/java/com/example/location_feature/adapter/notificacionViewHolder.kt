package com.example.b_notificacion.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.b_notificacion.model.notificaciones
import com.example.location_feature.R

class notificacionViewHolder(view: View):ViewHolder(view) {
    val dia =view.findViewById<TextView>(R.id.tvDia)
    val notifi =view.findViewById<TextView>(R.id.tvnotificacion)
    val des_notifi =view.findViewById<TextView>(R.id.tvDes_Notificación)
    val fecha =view.findViewById<TextView>(R.id.tvFecha)
    val imagen =view.findViewById<ImageView>(R.id.tvimgnotificacion)
    fun render(notificacionesModel: notificaciones){
        dia.text = notificacionesModel.dia
        notifi.text = notificacionesModel.notifi
        des_notifi.text = notificacionesModel.des_notifi
        fecha.text = notificacionesModel.fecha
        Glide.with(imagen.context).load(notificacionesModel.imagen).into(imagen)

    }
}