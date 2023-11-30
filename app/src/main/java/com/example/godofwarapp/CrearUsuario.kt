package com.example.godofwarapp

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.godofwarapp.databinding.ActivityCrearUsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_crear_usuario.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.txtEmail
import kotlinx.android.synthetic.main.activity_login.txtPass

class CrearUsuario : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_usuario)

        //Binding
        binding = ActivityCrearUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Auth
        auth = Firebase.auth
        //Setup
        setup()

    }

    private lateinit var binding: ActivityCrearUsuarioBinding;
    private lateinit var auth: FirebaseAuth;

    private fun setup(){
        binding.btnCrear.setOnClickListener{
            val mp = MediaPlayer.create(this, R.raw.sonido)
            mp.start()
                if (binding.txtEmail.text.isNotEmpty() && binding.txtPass.text.isNotEmpty()){
                    auth.createUserWithEmailAndPassword(binding.txtEmail.text.toString(), binding.txtPass.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_LONG).show()
                            showHome(it.result?.user?.email?:"")
                        } else {
                            Toast.makeText(this, "Error en la creación de usuario", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    private fun showHome(email: String){
        val homeIntent = Intent(this, Login::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }

}