package com.example.b_notificacion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.b_notificacion.R
import com.example.b_notificacion.model.notificaciones

class notificacionadapter(private val noticicacionesList:List<notificaciones>):RecyclerView.Adapter<notificacionViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notificacionViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        return notificacionViewHolder(layoutInflater.inflate(R.layout.item_notificacion,parent, false))
    }


    override fun onBindViewHolder(holder: notificacionViewHolder, position: Int) {
        val item =noticicacionesList[position]
        holder.render(item)

    }
    override fun getItemCount(): Int =noticicacionesList.size

}