package com.umbrella.ermolaevshiftapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umbrella.ermolaevshiftapp.data.model.PersonDataModel

@Dao
interface PersonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(person: PersonDataModel)

    @Query("SELECT * FROM persons WHERE login = :login")
    fun getPerson(login: String): PersonDataModel?
}