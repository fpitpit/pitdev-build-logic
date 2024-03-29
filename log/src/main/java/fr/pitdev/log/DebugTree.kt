package fr.pitdev.log

import timber.log.Timber

class DefaultDebugTree(private val debugMode: Boolean = false) : Timber.DebugTree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean = debugMode && super.isLoggable(tag, priority)

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
    }

}
