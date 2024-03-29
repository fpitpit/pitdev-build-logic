package com.example.tutojacocokts

import android.app.Application
import fr.pitdev.log.DefaultDebugTree
import fr.pitdev.log.DefaultLogProvider
import fr.pitdev.log.LogProvider


class TutoJacocoApplication : Application() {

    private val logProvider: LogProvider = DefaultLogProvider(debugTree = DefaultDebugTree(debugMode = BuildConfig.DEBUG))

    override fun onCreate() {
        super.onCreate()
        logProvider.create(debugMode = BuildConfig.DEBUG)
    }

    override fun onTerminate() {
        logProvider.terminate()
        super.onTerminate()
    }
}
