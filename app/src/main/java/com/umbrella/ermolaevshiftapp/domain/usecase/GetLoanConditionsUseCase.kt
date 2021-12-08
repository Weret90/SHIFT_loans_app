package com.umbrella.ermolaevshiftapp.domain.usecase

import com.umbrella.ermolaevshiftapp.domain.entity.LoanConditions
import com.umbrella.ermolaevshiftapp.domain.repository.LoansRepository

class GetLoanConditionsUseCase(private val repository: LoansRepository) {

    suspend operator fun invoke(token: String): LoanConditions {
        return repository.getLoanConditions(token)
    }
}