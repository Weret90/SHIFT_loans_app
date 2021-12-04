package com.umbrella.ermolaevshiftapp.domain.usecase

import com.umbrella.ermolaevshiftapp.domain.entity.RegistrationRequest
import com.umbrella.ermolaevshiftapp.domain.entity.RegistrationResponse
import com.umbrella.ermolaevshiftapp.domain.repository.LoansRepository

class ToRegisterUseCase(private val repository: LoansRepository) {

    suspend operator fun invoke(registrationRequest: RegistrationRequest): RegistrationResponse {
        return repository.toRegister(registrationRequest)
    }
}