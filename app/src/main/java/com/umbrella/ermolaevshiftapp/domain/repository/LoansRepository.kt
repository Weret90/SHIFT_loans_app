package com.umbrella.ermolaevshiftapp.domain.repository

import com.umbrella.ermolaevshiftapp.domain.entity.*

interface LoansRepository {

    suspend fun toRegister(auth: Auth): User
    suspend fun getAuthToken(auth: Auth): String
    suspend fun getLoanConditions(token: String): LoanConditions
    suspend fun createLoan(token: String, loanRequest: LoanRequest): Loan
    suspend fun getAllLoans(token: String): List<Loan>
//    fun getWelcomeMessage()
//    fun getLoan()
//    fun goToSuccessScreen()
//    fun goToErrorScreen()
//    fun getInfoAboutLoans()
}