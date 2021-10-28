package com.nextpass.nextiati.nextpass.state

import androidx.annotation.StringRes
import com.nextpass.nextiati.nextpass.R

data class EmptyState(
    @StringRes val title: Int,
    @StringRes val message: Int,
) {
    companion object {
        fun default() = EmptyState(R.string.no_data, R.string.no_data_phrase)
    }
}
