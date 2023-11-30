package com.example.godofwarapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.godofwarapp.databinding.ActivityCrearUsuarioBinding
import com.example.godofwarapp.databinding.ActivityMainBinding
import com.google.android.gms.ads.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.processNextEventInCurrentThread

class MainActivity : AppCompatActivity() {

    lateinit var mAdView : AdView

    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {

        //2 segundos para splashscreen
        Thread.sleep(2000)
        setTheme(R.style.Theme_GodofWarApp)

        super.onCreate(savedInstanceState)

        //Enviando data al analytics
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        analytics.logEvent("MainActivity", bundle)

        //auth
        auth = Firebase.auth

        //Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val mp = MediaPlayer.create(this, R.raw.sonido)
            mp.start()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        //Banner
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

        crearCanalNotificaciones()

        //Setup
        setup()
        //Session
        session()
    }

    val CHANNELID = "CANAL1"
    val notificationID = 100

    override fun onStart() {
        super.onStart()
        binding.ventanaLogin.visibility = View.VISIBLE
    }

    private fun session(){
        val prefs = getSharedPreferences("com.example.godofwarapp.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if(email != null && provider != null){
            binding.ventanaLogin.visibility = View.INVISIBLE
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun setup(){
        btnGoogle.setOnClickListener{
            //configuración

            val mp = MediaPlayer.create(this, R.raw.sonido)
            mp.start()

            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {

                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showHome(account.email ?: "", ProviderType.GOOGLE)
                                Toast.makeText(this,"Sesion iniciada", Toast.LENGTH_LONG).show()
                                enviarNotificacion()
                            } else {
                                Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                }
            } catch (e: ApiException){
                Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun showHome(email: String, provider: ProviderType){
        val homeIntent = Intent(this, HomeActivity ::class.java).apply{
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }

    private lateinit var binding: ActivityMainBinding;
    private lateinit var auth: FirebaseAuth;


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