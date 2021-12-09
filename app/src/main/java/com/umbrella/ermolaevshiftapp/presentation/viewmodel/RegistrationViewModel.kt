package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.domain.entity.User
import com.umbrella.ermolaevshiftapp.domain.usecase.ToRegisterUseCase
import com.umbrella.ermolaevshiftapp.presentation.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RegistrationViewModel(private val toRegisterUseCase: ToRegisterUseCase) : ViewModel() {

    private val _registrationLiveData = MutableLiveData<State<User>>()
    val registrationLiveData: LiveData<State<User>> get() = _registrationLiveData

    companion object {
        private const val ERROR_INPUT_DATA = "Введите корректные имя и пароль"
    }

    fun toRegister(name: String, password: String) {
        if (name.isNotBlank() && password.isNotBlank()) {
            _registrationLiveData.value = State.Loading
            val auth = Auth(name, password)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val user = toRegisterUseCase(auth)
                    withContext(Dispatchers.Main) {
                        _registrationLiveData.value = State.Success(user)
                    }
                } catch (httpException: HttpException) {
                    withContext(Dispatchers.Main) {
                        _registrationLiveData.value =
                            State.Error(httpException.response()?.errorBody()?.string())
                    }
                } catch (exception: Exception) {
                    withContext(Dispatchers.Main) {
                        _registrationLiveData.value = State.Error(exception.message)
                    }
                }
            }
        } else {
            _registrationLiveData.value = State.Error(ERROR_INPUT_DATA)
        }
    }

    fun clearRegistrationLiveData() {
        _registrationLiveData.value = null
    }
}