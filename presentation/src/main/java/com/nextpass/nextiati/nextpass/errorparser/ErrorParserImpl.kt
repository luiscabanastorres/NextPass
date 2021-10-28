package com.nextpass.nextiati.nextpass.errorparser

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ErrorParserImpl @Inject constructor() : ErrorParser {

    override fun parse(ex: Exception?): Pair<String, String> {
        if (ex != null) {
            Log.d("bugsApp",ex.localizedMessage)
        }
        if (ex is HttpException){
            if ((ex as HttpException).code()>499){
                return Pair("Error", "Ocurrió un error")
            }
            if ( (ex as HttpException).response()?.errorBody()!=null){
                val string = (ex as HttpException).response()!!.errorBody()!!.string() // The String which Need To Be Converted
                Log.d("errores",string)
                var responseStringError = "";
                responseStringError = try {
                    val jresponse = JSONObject(string)
                    jresponse.getString("error");
                }catch (ex : Exception){
                    val listResponse = JSONArray(string)
                    listResponse[0].toString().replace("Error:", "").trim()
                }

                return Pair("Error", responseStringError)
            }
        }else if (ex is UnknownHostException){
            return Pair("Error", "Revisa tu conexión a internet")
        }else if (ex is SocketTimeoutException){
            return Pair("Error", "Revisa tu conexión a internet")
        }
        return Pair("Error", "Ocurrió un error")

    }



}
