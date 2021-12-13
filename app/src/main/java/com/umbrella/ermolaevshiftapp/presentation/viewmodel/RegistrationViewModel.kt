package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.domain.entity.User
import com.umbrella.ermolaevshiftapp.domain.usecase.ToRegisterUseCase
import com.umbrella.ermolaevshiftapp.presentation.state.InputDataError
import com.umbrella.ermolaevshiftapp.presentation.state.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RegistrationViewModel(private val toRegisterUseCase: ToRegisterUseCase) : ViewModel() {

    private val _registrationLiveData = MutableLiveData<State<User>>()
    val registrationLiveData: LiveData<State<User>> get() = _registrationLiveData

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
                        _registrationLiveData.value = State.Error.ErrorResponse(
                            httpException.code(), httpException.response()?.errorBody()?.string()
                        )
                    }
                } catch (exception: Exception) {
                    withContext(Dispatchers.Main) {
                        _registrationLiveData.value = State.Error.UnknownError(exception)
                    }
                }
            }
        } else {
            _registrationLiveData.value = State.Error.InputError(InputDataError.EMPTY_INPUT_DATA)
        }
    }

    fun clearRegistrationLiveData() {
        _registrationLiveData.value = null
    }
}