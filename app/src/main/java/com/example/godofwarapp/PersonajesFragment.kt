package com.example.godofwarapp

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.godofwarapp.databinding.FragmentPersonajesBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class PersonajesFragment : Fragment() {

    //Llamamos a la BD
    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentPersonajesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_personajes, container, false)
        _binding = FragmentPersonajesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        binding.btnGuardar.setOnClickListener {
            if (binding.txtNombre.text.toString() != "nombre") {
                db.collection("personajes").document().set(
                    hashMapOf(
                        "nombre" to binding.txtNombre.text.toString(),
                        "nivel" to binding.txtNivel.text.toString(),
                        "poder" to binding.txtPoder.text.toString(),
                    )
                )
                Toast.makeText(context, "Personaje agregado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al agregar personaje", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnEliminar.setOnClickListener{
            db.collection("personajes").document(binding.txtNombre.text.toString()).delete()
        }

        binding.btnBuscar.setOnClickListener {
            db.collection("personajes").document(binding.txtNombre.text.toString()).get().addOnCompleteListener{
                binding.txtNombre.setText(it.result.get("nombre") as String?)
                binding.txtNivel.setText(it.result.get("nivel") as String?)
                binding.txtPoder.setText(it.result.get("poder") as String?)
            }
        }
    }
}