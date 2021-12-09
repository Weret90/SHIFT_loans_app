package com.umbrella.ermolaevshiftapp.domain.usecase

import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import com.umbrella.ermolaevshiftapp.domain.repository.LoansRepository

class GetAllLoansUseCase(private val repository: LoansRepository) {

    suspend operator fun invoke(token: String): List<Loan> {
        return repository.getAllLoans(token)
    }
}