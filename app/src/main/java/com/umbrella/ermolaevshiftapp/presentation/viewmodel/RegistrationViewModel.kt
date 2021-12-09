package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.domain.entity.User
import com.umbrella.ermolaevshiftapp.domain.usecase.ToRegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RegistrationViewModel(private val toRegisterUseCase: ToRegisterUseCase) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _success = MutableLiveData<User>()
    val success: LiveData<User> get() = _success

    fun toRegister(name: String, password: String) {
        if (name.isNotBlank() && password.isNotBlank()) {
            _loading.value = true
            val auth = Auth(name, password)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val user = toRegisterUseCase(auth)
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        _success.value = user
                    }
                } catch (httpException: HttpException) {
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        _error.value = httpException.response()?.errorBody()?.string()
                    }
                } catch (exception: Exception) {
                    withContext(Dispatchers.Main) {
                        _loading.value = false
                        _error.value = exception.message
                    }
                }
            }
        } else {
            _error.value = "Введите корректные имя и пароль"
        }
    }
}