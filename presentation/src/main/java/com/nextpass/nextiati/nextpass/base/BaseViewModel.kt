package com.nextpass.nextiati.nextpass.base

import android.app.Application
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.nextpass.nextiati.data.dispatcher.CoroutineDispatchers
import com.nextpass.nextiati.nextpass.errorparser.ErrorParser
import com.nextpass.nextiati.nextpass.state.ViewModelListResult
import com.nextpass.nextiati.nextpass.state.ViewModelResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import retrofit2.HttpException


abstract class BaseViewModel(
    protected val errorParser: ErrorParser,
    protected val coroutineDispatchers: CoroutineDispatchers,
    protected val savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(
    application,
) {

    protected fun <T> safeViewModelResultLaunch(
        dispatcher: CoroutineDispatcher = coroutineDispatchers.main,
        liveData: MutableLiveData<ViewModelResult<T>>,
        maybeLocal:Boolean = false,

        block: suspend () -> Unit,

    ) {
        viewModelScope.launch(dispatcher) {

            try {
                if (isNetworkAvailable || maybeLocal){
                    block()

                }else{
                    val viewModelResult = ViewModelResult.Error("Error","No hay internet")
                    liveData.value = viewModelResult

                }

            } catch (ex: Exception) {


                if (ex is HttpException) {
                    if ((ex as HttpException).code() == 401) {
                        var viewModelResult = ViewModelResult.Unauthorized("Error", "El token ha caducado")
                        liveData.value = viewModelResult
                    }else{
                        val error = errorParser.parse(ex)
                        var viewModelResult = ViewModelResult.Error(error.first, error.second)
                        liveData.value = viewModelResult
                    }
                }


            }
        }
    }

    protected fun <T> safeViewModelListResultLaunch(
        dispatcher: CoroutineDispatcher = coroutineDispatchers.main,
        liveData: MutableLiveData<ViewModelListResult<T>>,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                block()
            } catch (ex: Exception) {
                val error = errorParser.parse(ex)
                val listResult = ViewModelListResult.Error(error.first, error.second)
                liveData.value = listResult
            }
        }
    }
    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getApplication<Application>().applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val capabilities =
                        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            return true
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            return true
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                            return true
                        }
                    }
                }
            }
            return false
        }

}
