package com.nextpass.nextiati.nextpass.di

import androidx.annotation.StringRes

data class PresentationModuleError(
    @StringRes
    val title: Int,
    @StringRes
    val message: Int,
)
