package com.nextpass.nextiati.domain.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TemporyQrResponse (
    val encrypted_string: String
)