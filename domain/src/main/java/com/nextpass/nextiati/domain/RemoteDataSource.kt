package com.nextpass.nextiati.domain

import com.nextpass.nextiati.domain.entities.TemporyQrResponse
import com.nextpass.nextiati.domain.state.Result

interface RemoteDataSource {
    suspend fun getTemporyQR(id: Int): Result<TemporyQrResponse>
}
