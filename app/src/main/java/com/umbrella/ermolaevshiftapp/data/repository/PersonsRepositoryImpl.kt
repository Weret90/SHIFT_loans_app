package com.umbrella.ermolaevshiftapp.data.repository

import com.umbrella.ermolaevshiftapp.data.database.PersonsDao
import com.umbrella.ermolaevshiftapp.data.mapper.toDataModel
import com.umbrella.ermolaevshiftapp.data.mapper.toDomainModel
import com.umbrella.ermolaevshiftapp.domain.entity.Person
import com.umbrella.ermolaevshiftapp.domain.repository.PersonsRepository

class PersonsRepositoryImpl(private val dao: PersonsDao) : PersonsRepository {

    override fun getPerson(login: String): Person? {
        return dao.getPerson(login)?.toDomainModel()
    }

    override fun insertPerson(person: Person) {
        dao.insertPerson(person.toDataModel())
    }
}