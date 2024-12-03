package com.carlostorres.wordsgame.utils

sealed class ViewState<out T> {

    class Success<T>(val data: T?) : ViewState<T>()
    class Error(val message: String) : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()

}