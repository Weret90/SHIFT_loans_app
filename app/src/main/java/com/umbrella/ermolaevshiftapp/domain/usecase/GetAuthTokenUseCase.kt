package com.umbrella.ermolaevshiftapp.domain.usecase

import com.umbrella.ermolaevshiftapp.domain.entity.Auth
import com.umbrella.ermolaevshiftapp.domain.repository.LoansRepository

class GetAuthTokenUseCase(private val repository: LoansRepository) {

    suspend operator fun invoke(auth: Auth): String {
        return repository.getAuthToken(auth)
    }
}