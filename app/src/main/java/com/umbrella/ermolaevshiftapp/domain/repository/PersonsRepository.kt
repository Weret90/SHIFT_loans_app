package com.umbrella.ermolaevshiftapp.domain.repository

import com.umbrella.ermolaevshiftapp.domain.entity.Person

interface PersonsRepository {

    fun getPerson(login: String): Person?
    fun insertPerson(person: Person)
}