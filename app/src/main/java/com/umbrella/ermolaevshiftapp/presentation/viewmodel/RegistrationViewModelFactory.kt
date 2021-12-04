package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umbrella.ermolaevshiftapp.domain.usecase.ToRegisterUseCase

class RegistrationViewModelFactory(
    private val toRegisterUseCase: ToRegisterUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegistrationViewModel(toRegisterUseCase) as T
    }
}