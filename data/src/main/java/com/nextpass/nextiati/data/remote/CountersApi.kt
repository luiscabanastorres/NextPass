package com.nextpass.nextiati.data.remote

import com.nextpass.nextiati.domain.entities.TemporyQrResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

const val BASE_URL_QA =     "https://nextwms-senado-staging.herokuapp.com/api/"
const val BASE_URL_PROD =   "https://api.nextwms.mx/api/"

interface Api {
    @GET
    suspend fun getTemporyQR(
        @Url url: String,
        @Header("Authorization") header: String
    ): TemporyQrResponse

}