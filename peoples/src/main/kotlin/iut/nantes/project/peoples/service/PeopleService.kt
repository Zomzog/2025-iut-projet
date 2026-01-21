package iut.nantes.project.peoples.service

import iut.nantes.project.peoples.repository.Address
import iut.nantes.project.peoples.repository.People
import iut.nantes.project.peoples.repository.PeopleDatabase

class PeopleService(
    private val database: PeopleDatabase
){

    private fun resolveAddress(incomingAddress: Address): Address {
        val existing = database.findAddress(
            incomingAddress.street, incomingAddress.city, incomingAddress.zipCode, incomingAddress.country
        )
        return existing ?: incomingAddress
    }

    fun createPeople(people: People): People {
        val peopleWithAddress = people.copy(address = resolveAddress(people.address))
        return database.save(peopleWithAddress)
    }

    fun updatePeople(id: Long, newInfo: People): People? {
        val existingPeople = database.findById(id) ?: return null

        val properAddress = resolveAddress(newInfo.address)

        val updatedPeople = existingPeople.copy(
            firstName = newInfo.firstName,
            lastName = newInfo.lastName,
            age = newInfo.age,
            address = properAddress
        )
        return database.save(updatedPeople)
    }

    fun deletePeople(id: Long): Boolean {
        return database.deleteById(id) > 0
    }
    fun findPeople(name: String?): List<People> {
        return database.findAll(name)
    }

    fun findPeopleById(id: Long): People? {
        return database.findById(id)
    }
}