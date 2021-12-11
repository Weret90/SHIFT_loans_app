package com.umbrella.ermolaevshiftapp.domain.usecase

import com.umbrella.ermolaevshiftapp.domain.entity.Person
import com.umbrella.ermolaevshiftapp.domain.repository.PersonsRepository

class InsertPersonUseCase(private val repository: PersonsRepository) {

    operator fun invoke(person: Person) {
        repository.insertPerson(person)
    }
}