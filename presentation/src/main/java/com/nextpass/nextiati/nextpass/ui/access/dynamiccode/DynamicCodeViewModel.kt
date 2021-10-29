package com.nextpass.nextiati.nextpass.ui.access.dynamiccode

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.nextpass.nextiati.data.Repository
import com.nextpass.nextiati.data.dispatcher.CoroutineDispatchers
import com.nextpass.nextiati.data.preferences.Preferences
import com.nextpass.nextiati.domain.entities.TemporyQrResponse
import com.nextpass.nextiati.nextpass.base.BaseViewModel
import com.nextpass.nextiati.nextpass.errorparser.ErrorParser
import com.nextpass.nextiati.nextpass.state.ViewModelResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.nextpass.nextiati.domain.state.Result
import com.nextpass.nextiati.nextpass.extensions.viewModel

class DynamicCodeViewModel @ViewModelInject constructor(
    private val repository: Repository,
    errorParser: ErrorParser,
    coroutineDispatchers: CoroutineDispatchers,
    @Assisted savedStateHandle: SavedStateHandle,
    application: Application,
    private val preferences: Preferences,
) : BaseViewModel(
    errorParser,
    coroutineDispatchers,
    savedStateHandle,
    application,
) {
    private val _dynamicCodeResponse = MutableLiveData<ViewModelResult<TemporyQrResponse>>()
    val dynamicCodeResponse: LiveData<ViewModelResult<TemporyQrResponse>>
        get() = _dynamicCodeResponse

    fun getDynamicCode() {
        _dynamicCodeResponse.value = Result.Loading.viewModel(
            errorParser = errorParser,
        )
        //Test
        preferences.saveToken("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5Iiwic2NwIjoidXNlciIsImF1ZCI6bnVsbCwiaWF0IjoxNjM1NDYyNDcyLCJleHAiOjE3OTMyNDcyMzIsImp0aSI6ImFjNGM4NjVmLTFjYjctNDM4YS05YTZlLWRkODJjNTUwMTdhNCJ9.Af-ttpDTWi9AgwnxAvTaM7AjlOIr_kihmjZPl6JvkXk")
        safeViewModelResultLaunch(Dispatchers.Main, _dynamicCodeResponse) {
            withContext(coroutineDispatchers.io) {
                val result = repository.getTemporyQR(9)
                withContext(coroutineDispatchers.main) {
                    _dynamicCodeResponse.value = result.viewModel(errorParser)
                }
            }
        }

    }
}