package com.umbrella.ermolaevshiftapp.di

import com.umbrella.ermolaevshiftapp.domain.usecase.*
import org.koin.dsl.module

object DomainDi {

    val useCasesModule = module {
        factory {
            CreateLoanUseCase(repository = get())
        }

        factory {
            GetAllLoansUseCase(repository = get())
        }

        factory {
            GetAuthTokenUseCase(repository = get())
        }

        factory {
            GetLoanConditionsUseCase(repository = get())
        }

        factory {
            ToRegisterUseCase(repository = get())
        }
    }
}