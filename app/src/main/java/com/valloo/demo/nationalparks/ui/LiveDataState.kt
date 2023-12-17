package com.valloo.demo.nationalparks.ui

import androidx.annotation.StringRes

sealed class LiveDataState<T> {
    data class Success<T>(val data: T) : LiveDataState<T>()
    data class Error<T>(@StringRes val errorMessageId: Int) : LiveDataState<T>()
    class Loading<T> : LiveDataState<T>()
}

