package com.example.godofwarapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthProvider

class LoginGithun : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var loginBtn: Button
    private lateinit var githubEdit: EditText

    //Llamamos a auth
    private lateinit var auth: FirebaseAuth

    //Llamamos al provider
    private val provider = OAuthProvider.newBuilder("github.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_githun)

        loginBtn = findViewById(R.id.btnLogingit)
        githubEdit = findViewById(R.id.githubId)

        //inicializando auth
        auth = FirebaseAuth.getInstance()

        //Especificación email
        provider.addCustomParameter("login", githubEdit.text.toString())

        //Preconfigurar en la API permisos
        val scopes: ArrayList<String?> = object : ArrayList<String?>(){
            init {
                add("user:email")
            }
        }
        provider.scopes = scopes

        loginBtn.setOnClickListener(){
            if (TextUtils.isEmpty(githubEdit.text.toString())){
                Toast.makeText(this, "Selecciona tu Github", Toast.LENGTH_LONG).show()
            } else {
                signInWithGithubProvider()
            }
        }
    }

    private fun signInWithGithubProvider(){
        val pendingResultTask: Task<AuthResult>? = auth.pendingAuthResult
        if (pendingResultTask != null){
            pendingResultTask.addOnSuccessListener {
                Toast.makeText(this, "Usuario existe", Toast.LENGTH_LONG).show()
            }
                .addOnSuccessListener {
                    Toast.makeText(this, "Error : $it", Toast.LENGTH_LONG).show()
                }
        } else {
            auth.startActivityForSignInWithProvider(this, provider.build()).addOnSuccessListener(
                OnSuccessListener<AuthResult?> {
                    firebaseUser = auth.currentUser!!

                    val intent = Intent(this, HomeActivity::class.java)
                    this.startActivity(intent)
                    Toast.makeText(this, "Sesión iniciada", Toast.LENGTH_LONG).show()
                }).addOnFailureListener(OnFailureListener {Toast.makeText(this,"Error : $it", Toast.LENGTH_LONG).show()})
        }
    }
}