package com.umbrella.ermolaevshiftapp.domain.repository

import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.domain.entity.User

interface LoansRepository {

    suspend fun toRegister(auth: Auth): User
    suspend fun getAuthToken(auth: Auth): String
//    fun getWelcomeMessage()
//    fun getLoan()
//    fun goToSuccessScreen()
//    fun goToErrorScreen()
//    fun getInfoAboutLoans()
}