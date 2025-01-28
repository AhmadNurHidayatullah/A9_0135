package com.example.projectfilm_135.repository

import com.example.projectfilm_135.sevice.FilmService
import com.example.projectfilm_135.sevice.PenayanganService
import com.example.projectfilm_135.sevice.StudioService
import com.example.projectfilm_135.sevice.TiketService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val filmRepository: FilmRepository
    val studioRepository: StudioRepository
    val penayanganRepository:PenayanganRepository
    val tiketRepository:TiketRepository
}

class BioskopContainer : AppContainer {
    private val baseUrl = "http://10.0.2.2/ProjekFilmA9/"
    private val json = Json { ignoreUnknownKeys = true }

    // Membuat instance Retrofit dengan konfigurasi JSON
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    // Membuat instance FilmService menggunakan Retrofit
    private val filmService: FilmService by lazy {
        retrofit.create(FilmService::class.java)
    }

    // Membuat instance StudioService menggunakan Retrofit
    private val studioService: StudioService by lazy {
        retrofit.create(StudioService::class.java)
    }

    // Membuat instance PenayanganService menggunakan Retrofit
    private val penayanganService: PenayanganService by lazy {
        retrofit.create(PenayanganService::class.java)
    }

    private val tiketService: TiketService by lazy {
        retrofit.create(TiketService::class.java)
    }

    // Implementasi FilmRepository menggunakan NetworkFilmRepository
    override val filmRepository: FilmRepository by lazy {
        NetworkFilmRepository(filmService)
    }

    // Implementasi StudioRepository menggunakan NetworkStudioRepository
    override val studioRepository: StudioRepository by lazy {
        NetworkStudioRepository(studioService)
    }

    // Implementasi StudioRepository menggunakan NetworkStudioRepository
    override val penayanganRepository: PenayanganRepository by lazy {
        NetworkPenayanganRepository(penayanganService)
    }

    override val tiketRepository: TiketRepository by lazy {
        NetworkTiketRepository(tiketService)
    }
}
