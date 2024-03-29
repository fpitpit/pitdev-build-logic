package fr.pitdev.log

import timber.log.Timber

interface LogProvider {
    fun create(debugMode: Boolean = false)
    fun terminate()
}

class DefaultLogProvider(private val debugTree: Timber.DebugTree): LogProvider {
    override fun create(debugMode: Boolean) = Timber.plant(debugTree)
    override fun terminate() = Timber.uprootAll()
}
