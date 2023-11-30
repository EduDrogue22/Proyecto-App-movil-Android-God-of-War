package com.example.godofwarapp

data class InfoPersonajeItem(
    val Alies: List<String>,
    val about: String,
    val appearsIn: List<String>,
    val character: String,
    val gender: String,
    val id: Int,
    val img: String,
    val quote: String,
    val species: List<String>,
    val voiceActor: String
)