package com.umbrella.ermolaevshiftapp.domain.usecase

import com.umbrella.ermolaevshiftapp.domain.entity.Person
import com.umbrella.ermolaevshiftapp.domain.repository.PersonsRepository

class GetPersonUseCase(private val repository: PersonsRepository) {

    operator fun invoke(login: String): Person? {
        return repository.getPerson(login)
    }
}