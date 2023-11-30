package com.example.godofwarapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PersonajeAdapter(private val lista:List<InfoPersonajeItem>): RecyclerView.Adapter<InfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return InfoViewHolder(layoutInflater.inflate(R.layout.item_personaje, parent, false))
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int){
        val item = lista[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = lista.size
}