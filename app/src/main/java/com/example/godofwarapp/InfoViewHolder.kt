package com.example.godofwarapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.godofwarapp.databinding.ItemPersonajeBinding
import com.squareup.picasso.Picasso

class InfoViewHolder(view:View):RecyclerView.ViewHolder(view) {

    private val binding = ItemPersonajeBinding.bind(view)

    val auxNombre = view.findViewById<TextView>(R.id.txtNombre)
    val auxEspecie = view.findViewById<TextView>(R.id.txtEspecie)
    val auxGenero = view.findViewById<TextView>(R.id.txtGenero)

    fun render(infoResponseItemModel: InfoPersonajeItem){
        auxNombre.text = infoResponseItemModel.character
        auxEspecie.text = infoResponseItemModel.species.toString()
        auxGenero.text = infoResponseItemModel.gender
        Picasso.get().load(infoResponseItemModel.img).into(binding.txtImagen)
    }
}