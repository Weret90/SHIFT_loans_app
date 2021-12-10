package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.domain.usecase.GetAuthTokenUseCase
import com.umbrella.ermolaevshiftapp.presentation.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AuthorizationViewModel(
    private val getAuthTokenUseCase: GetAuthTokenUseCase,
) : ViewModel() {

    private val _authorizationLiveData = MutableLiveData<State<Pair<Auth, String>>>()
    val authorizationLiveData: LiveData<State<Pair<Auth, String>>> get() = _authorizationLiveData

    companion object {
        private const val ERROR_EMPTY_FIELDS = "All fields must be filled"
    }

    fun toEnter(name: String, password: String) {
        if (name.isNotBlank() && password.isNotBlank()) {
            _authorizationLiveData.value = State.Loading
            val auth = Auth(name, password)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val token = getAuthTokenUseCase(auth)
                    withContext(Dispatchers.Main) {
                        _authorizationLiveData.value = State.Success(auth to token)
                    }
                } catch (httpException: HttpException) {
                    withContext(Dispatchers.Main) {
                        _authorizationLiveData.value =
                            State.Error(httpException.response()?.errorBody()?.string())
                    }
                } catch (exception: Exception) {
                    withContext(Dispatchers.Main) {
                        _authorizationLiveData.value = State.Error(exception.message)
                    }
                }
            }
        } else {
            _authorizationLiveData.value = State.Error(ERROR_EMPTY_FIELDS)
        }
    }

    fun clearAuthorizationLiveData() {
        _authorizationLiveData.value = null
    }
}