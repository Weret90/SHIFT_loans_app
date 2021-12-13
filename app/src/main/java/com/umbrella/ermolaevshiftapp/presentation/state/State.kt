package com.umbrella.ermolaevshiftapp.presentation.state

import java.lang.Exception

sealed class State<out T> {

    object Loading : State<Nothing>()

    sealed class Error : State<Nothing>() {
        data class ErrorResponse(val errorCode: Int, val errorBody: String?) : Error()
        data class InputError(val inputDataError: InputDataError) : Error()
        data class UnknownError(val exception: Exception) : Error()
    }

    data class Success<T>(val data: T) : State<T>()
}
