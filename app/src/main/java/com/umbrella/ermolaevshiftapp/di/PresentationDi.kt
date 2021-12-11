package com.umbrella.ermolaevshiftapp.di

import com.umbrella.ermolaevshiftapp.presentation.viewmodel.AuthorizationViewModel
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.CreateLoanViewModel
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.LoansViewModel
import com.umbrella.ermolaevshiftapp.presentation.viewmodel.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object PresentationDi {

    val viewModelsModule = module {

        viewModel {
            AuthorizationViewModel(getAuthTokenUseCase = get())
        }

        viewModel {
            CreateLoanViewModel(
                getLoanConditionsUseCase = get(),
                createLoanUseCase = get(),
                getPersonUseCase = get(),
                insertPersonUseCase = get()
            )
        }

        viewModel {
            LoansViewModel(getAllLoansUseCase = get())
        }

        viewModel {
            RegistrationViewModel(toRegisterUseCase = get())
        }
    }
}
