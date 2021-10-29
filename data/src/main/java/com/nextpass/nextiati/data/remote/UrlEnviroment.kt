package com.nextpass.nextiati.data.remote

const val IS_PRODUCTION = false
class UrlEnviroment {
    companion object {
        val BASE_URL =  if(IS_PRODUCTION) "https://app.nextpass.mx/api/v1/" else "https://nextpass-staging.herokuapp.com/api/v1/"
        val AUTH_O_BASE_URL = if(IS_PRODUCTION) "https://app.nextpass.mx/" else "https://nextpass-staging.herokuapp.com/"
    }
}