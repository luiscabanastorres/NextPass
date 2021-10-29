package com.nextpass.nextiati.data.preferences

interface Preferences {
    fun getToken(): String?
    fun saveToken(token: String?)
}