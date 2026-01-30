package iut.nantes.project.peoples

import iut.nantes.project.peoples.domain.People
import iut.nantes.project.peoples.domain.Address

interface PeopleRepository {
    fun findAll(): List<People>
    fun findOne(id: Long): People?
    fun saveOne(people: People): People
    fun saveOneWithGeneratedId(people: People): People
    fun deleteOne(id: Long): Boolean
    fun findAddressByCity(city: String): List<Address>
    fun findAllFilteredByName(name: String?): List<People>
    fun updateOne(id: Long, people: People): People?
}
