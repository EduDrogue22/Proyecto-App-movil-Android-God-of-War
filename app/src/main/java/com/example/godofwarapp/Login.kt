package com.example.godofwarapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.godofwarapp.databinding.ActivityCrearUsuarioBinding
import com.example.godofwarapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //auth
        auth = Firebase.auth

        //setup
        setup()

        binding.btnCrearUsuario.setOnClickListener { val intent = Intent(this, CrearUsuario::class.java)
            startActivity(intent)
            val mp = MediaPlayer.create(this, R.raw.sonido)
            mp.start()
        }

        crearCanalNotificaciones()

    }

    private lateinit var auth: FirebaseAuth;
    private lateinit var binding: ActivityLoginBinding;

    //Notificaciones: Debemos crear el canal para enviar las notificaciones

    val CHANNELID = "CANAL1"
    val notificationID = 100

    override fun onStart() {
        super.onStart()
        binding.ventanaLogin.visibility = View.VISIBLE
    }

    //Función login
    private fun setup(){

        val prefs = getSharedPreferences("com.example.godofwarapp.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val correo = prefs.getString("CORREO", null)
        val proveedor = prefs.getString("PROVEEDOR", null)

        if ( correo != null && proveedor != null ){
            binding.ventanaLogin.visibility = View.INVISIBLE
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.btnLogear.setOnClickListener {
            val mp = MediaPlayer.create(this, R.raw.sonido)
            mp.start()
            if(binding.txtEmail.text.isNotEmpty() && binding.txtPass.text.isNotEmpty()){
                auth.signInWithEmailAndPassword(binding.txtEmail.text.toString(), binding.txtPass.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Sesion iniciada", Toast.LENGTH_LONG).show()
                        showHome(it.result?.user?.email?:" ")
                        enviarNotificacion()

                    } else{
                        Toast.makeText(this, "El usuario no existe", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun showHome(email: String){
        val homeIntent = Intent(this, HomeActivity ::class.java).apply{
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }

    private fun crearCanalNotificaciones(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val nombreTitulo = "Titulo Notificación"
            val descripcion = "Mensaje de la notificacion"
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel(CHANNELID, nombreTitulo, importancia).apply {
                description = descripcion
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }

    private fun enviarNotificacion(){
        val builder = NotificationCompat.Builder(this, CHANNELID)
            .setContentTitle("Bienvenido")
            .setContentText("Bienvenido a God of War APP")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.icono_boton)
        with(NotificationManagerCompat.from(this)){
            notify(notificationID, builder.build())
        }
    }


}