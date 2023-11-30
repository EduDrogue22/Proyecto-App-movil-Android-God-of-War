package com.example.godofwarapp

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.godofwarapp.databinding.FragmentArmasBinding
import com.example.godofwarapp.databinding.FragmentPersonajesBinding
import com.google.firebase.firestore.FirebaseFirestore

class ArmasFragment : Fragment() {

    //Llamamos a la BD
    private val db = FirebaseFirestore.getInstance()
    private var _binding: FragmentArmasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_armas, container, false)
        _binding = FragmentArmasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        binding.btnGuardar.setOnClickListener {
            if (binding.txtArma.text.toString() != "arma") {
                db.collection("armas y magias").document().set(
                    hashMapOf(
                        "arma" to binding.txtArma.text.toString(),
                        "poder" to binding.txtPoderArm.text.toString(),
                    )
                )
                Toast.makeText(context, "Arma/magia agregado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al agregar arma/magia", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnEliminar.setOnClickListener {
            db.collection("armas y magias").document(binding.txtArma.text.toString()).delete().addOnCompleteListener{

                if(it.isSuccessful){
                    Toast.makeText(context, "Arma/magia eliminada", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnBuscar.setOnClickListener {
            db.collection("armas y magias").document(binding.txtArma.text.toString()).get().addOnCompleteListener{
                binding.txtArma.setText(it.result.get("arma") as String?)
                binding.txtPoderArm.setText(it.result.get("poder") as String?)
            }
        }
    }

}