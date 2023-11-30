package com.example.godofwarapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.godofwarapp.databinding.FragmentListaPBinding
import com.example.godofwarapp.models.Personajesbd
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class PersonajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class ListaPFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentListaPBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_lista_p, container, false)
        _binding = FragmentListaPBinding.inflate(inflater, container, false)
        return binding.root
    }

    //
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
        val query = db.collection("personajes")
        val options = FirestoreRecyclerOptions.Builder<Personajesbd>().setQuery(query,Personajesbd::class.java).setLifecycleOwner(this).build()

        val adapter = object: FirestoreRecyclerAdapter<Personajesbd, PersonajeViewHolder>(options){
            //DONDE ACCEDEMOS EN LA DATA
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonajeViewHolder {
                val view = LayoutInflater.from(context).inflate(R.layout.row_personajes, parent, false)
                return PersonajeViewHolder(view)
            }

            //DONDE CARGAMOS LA DATA
            override fun onBindViewHolder(holder: PersonajeViewHolder, position: Int, model: Personajesbd) {
                val nombre: TextView = holder.itemView.findViewById(R.id.txtViewPersonaje)
                val nivel: TextView = holder.itemView.findViewById(R.id.txtViewNivel)
                val poder: TextView = holder.itemView.findViewById(R.id.txtViewPoder)
                nombre.text = model.nombre
                nivel.text = model.nivel
                poder.text = model.poder
            }
        }

        binding.rvPersonajes.adapter = adapter
        binding.rvPersonajes.layoutManager = LinearLayoutManager(context)
    }
}