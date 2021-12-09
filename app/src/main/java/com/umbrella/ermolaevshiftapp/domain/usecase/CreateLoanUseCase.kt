package com.umbrella.ermolaevshiftapp.domain.usecase

import com.umbrella.ermolaevshiftapp.domain.entity.Loan
import com.umbrella.ermolaevshiftapp.domain.entity.LoanRequest
import com.umbrella.ermolaevshiftapp.domain.repository.LoansRepository

class CreateLoanUseCase(private val repository: LoansRepository) {

    suspend operator fun invoke(token: String, loanRequest: LoanRequest): Loan {
        return repository.createLoan(token, loanRequest)
    }
}