package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umbrella.ermolaevshiftapp.domain.usecase.GetAuthTokenUseCase

class AuthorizationViewModelFactory(
    private val getAuthTokenUseCase: GetAuthTokenUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthorizationViewModel(getAuthTokenUseCase) as T
    }
}