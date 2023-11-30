package com.example.godofwarapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.godofwarapp.databinding.ActivityPersonajesBinding
import com.example.godofwarapp.databinding.FragmentPersonajesBinding
import com.google.firebase.firestore.FirebaseFirestore


class Personajes : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personajes)


    }

}