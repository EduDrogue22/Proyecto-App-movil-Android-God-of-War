package com.example.godofwarapp

import android.app.Person
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.godofwarapp.databinding.ActivityInicioBinding
import com.example.godofwarapp.databinding.ActivityLoginBinding
import com.example.godofwarapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_inicio.*

enum class ProviderType{
    GOOGLE
}

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_inicio

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        // Guardado de datos
        val prefs = getSharedPreferences("com.example.godofwarapp.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        //binding
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val listaFragment = ListaPFragment()
        val personajesFragment = PersonajesFragment()
        val armaFragment = ArmasFragment()

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_Frangment -> {
                    setCurrentFragment(homeFragment)
                    true
                }
                R.id.lista_Fragment -> {
                    setCurrentFragment(listaFragment)
                    true
                }
                R.id.perosnajes_Fragment -> {
                    setCurrentFragment(personajesFragment)
                    true
                }
                R.id.armas_Frangments -> {
                    setCurrentFragment(armaFragment)
                    true
                }

                else -> false
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_usuario, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.item_configuracion -> {
                setCurrentFragment(SettingsFragment())
                true
            }

            R.id.item_lista_armas -> {
                setCurrentFragment(ListaAFragment())
                true
            }

            R.id.item_lista -> {
                setCurrentFragment(InfoFragment())
                true
            }

            R.id.item_logout -> {
                val prefs = getSharedPreferences("com.example.godofwarapp.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit()
                prefs.clear()
                prefs.apply()
                FirebaseAuth.getInstance().signOut()
                onBackPressed()
                Toast.makeText(this,"Sesion cerrada", Toast.LENGTH_LONG).show()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private lateinit var binding: ActivityInicioBinding;

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerView, fragment)
            commit()
        }
    }


}