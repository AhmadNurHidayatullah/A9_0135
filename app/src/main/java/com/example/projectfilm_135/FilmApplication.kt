package com.example.projectfilm_135

import android.app.Application
import com.example.projectfilm_135.repository.AppContainer
import com.example.projectfilm_135.repository.BioskopContainer

class FilmApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = BioskopContainer()
    }
}