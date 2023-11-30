package com.example.godofwarapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.godofwarapp.databinding.FragmentListaABinding
import com.example.godofwarapp.databinding.FragmentListaPBinding
import com.example.godofwarapp.models.Armasbd
import com.example.godofwarapp.models.Personajesbd
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class ArmaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class ListaAFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentListaABinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_lista_a, container, false)
        _binding = FragmentListaABinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
        val query = db.collection("armas y magias")
        val options = FirestoreRecyclerOptions.Builder<Armasbd>().setQuery(query,
            Armasbd::class.java).setLifecycleOwner(this).build()

        val adapter = object: FirestoreRecyclerAdapter<Armasbd, ArmaViewHolder>(options){
            //Donde accedemos el que ve los txt
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmaViewHolder {
                val view = LayoutInflater.from(context).inflate(R.layout.row_armas, parent,false)
                return ArmaViewHolder(view)
            }

            //Donde cargamos la data
            override fun onBindViewHolder(holder: ArmaViewHolder, position: Int, model: Armasbd) {
                val arma: TextView = holder.itemView.findViewById(R.id.txtViewArma)
                val poder: TextView = holder.itemView.findViewById(R.id.txtViewPoderArm)
                arma.text = model.arma
                poder.text = model.poder
            }

        }

        binding.rvArmas.adapter = adapter
        binding.rvArmas.layoutManager = LinearLayoutManager(context)
    }

}