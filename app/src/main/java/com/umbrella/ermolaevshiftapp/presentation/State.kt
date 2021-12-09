package com.umbrella.ermolaevshiftapp.presentation

sealed class State<out T> {
    object Loading : State<Nothing>()
    data class Error(val errorMessage: String?) : State<Nothing>()
    data class Success<T>(val data: T) : State<T>()
}
