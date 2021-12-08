package com.umbrella.ermolaevshiftapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umbrella.ermolaevshiftapp.domain.usecase.CreateLoanUSeCase
import com.umbrella.ermolaevshiftapp.domain.usecase.GetLoanConditionsUseCase

class CreateLoanViewModelFactory(
    private val getLoanConditionsUseCase: GetLoanConditionsUseCase,
    private val createLoanUSeCase: CreateLoanUSeCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateLoanViewModel(getLoanConditionsUseCase, createLoanUSeCase) as T
    }
}