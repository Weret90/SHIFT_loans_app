package com.umbrella.ermolaevshiftapp.domain.usecase

import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.domain.entity.User
import com.umbrella.ermolaevshiftapp.domain.repository.LoansRepository

class ToRegisterUseCase(private val repository: LoansRepository) {

    suspend operator fun invoke(auth: Auth): User {
        return repository.toRegister(auth)
    }
}