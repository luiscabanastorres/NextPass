package com.nextpass.nextiati.domain.state

open class Event<out T>(private val data: T) {

    var handled = false
        private set

    fun getDataIfNotHandled(): T? =
        if (handled) {
            null
        } else {
            handled = true
            data
        }

    fun peekContent(): T = data

}
