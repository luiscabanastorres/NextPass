package com.nextpass.nextiati.data.remote

import com.nextpass.nextiati.data.dispatcher.CoroutineDispatchers
import com.nextpass.nextiati.data.preferences.Preferences
import com.nextpass.nextiati.domain.RemoteDataSource
import com.nextpass.nextiati.domain.state.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val countersApi: Api,
    private val preferences: Preferences,
) : RemoteDataSource {


    private suspend fun <T> safeApiCall(
            block: suspend () -> T,
            dispatcher: CoroutineDispatcher = coroutineDispatchers.default,
    ): Result<T> =
            withContext(dispatcher) {
                try {
                    Result.Success(block())
                } catch (ex: Exception) {
                    if (ex is retrofit2.HttpException) {
                        if ((ex as retrofit2.HttpException).code() == 401) {
                            Result.Unauthorized(ex)
                        }else{
                            Result.Error(ex)
                        }
                    }else{
                        Result.Error(ex)
                    }

                }
            }
}
