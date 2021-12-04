package com.umbrella.ermolaevshiftapp.domain.repository

import com.umbrella.ermolaevshiftapp.domain.entity.RegistrationRequest
import com.umbrella.ermolaevshiftapp.domain.entity.RegistrationResponse

interface LoansRepository {

    suspend fun toRegister(registrationRequest: RegistrationRequest): RegistrationResponse
//    fun toEnter()
//    fun getWelcomeMessage()
//    fun getLoan()
//    fun goToSuccessScreen()
//    fun goToErrorScreen()
//    fun getInfoAboutLoans()
}