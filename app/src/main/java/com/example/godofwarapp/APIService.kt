package com.example.godofwarapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET()
    suspend fun getInfo(@Url url:String): Response<List<InfoPersonajeItem>>
}