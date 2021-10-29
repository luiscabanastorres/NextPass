package com.nextpass.nextiati.data

import com.nextpass.nextiati.domain.RemoteDataSource
import com.nextpass.nextiati.domain.entities.TemporyQrResponse
import com.nextpass.nextiati.domain.state.Result
import javax.inject.Inject

class Repository@Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    suspend fun getTemporyQR(id: Int): Result<TemporyQrResponse> =
        remoteDataSource.getTemporyQR(id)
}