package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umbrella.ermolaevshiftapp.domain.usecase.GetAllLoansUseCase

class LoansViewModelFactory(
    private val getAllLoansUseCase: GetAllLoansUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoansViewModel(getAllLoansUseCase) as T
    }
}