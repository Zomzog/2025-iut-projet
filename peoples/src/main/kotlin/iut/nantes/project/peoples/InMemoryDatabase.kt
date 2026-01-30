package iut.nantes.project.peoples

import iut.nantes.project.peoples.domain.Address
import iut.nantes.project.peoples.domain.People
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

@Primary
@Profile("!prod")
@Repository
class InMemoryDatabase : PeopleRepository {
    private val peoples = mutableMapOf<Long, People>()

    override fun findAll(): List<People> = peoples.values.toList()

    override fun findOne(id: Long): People? = peoples[id]

    override fun saveOne(people: People): People {
        peoples[people.id] = people
        return people
    }

    override fun saveOneWithGeneratedId(people: People): People {
        val nextId = (peoples.keys.maxOrNull() ?: 0L) + 1L
        val toSave = people.copy(id = nextId)
        peoples[nextId] = toSave
        return toSave
    }

    override fun deleteOne(id: Long): Boolean = peoples.remove(id) != null

    override fun findAddressByCity(city: String): List<Address> =
        peoples.values.filter { it.address.city == city }.map { it.address }

    override fun findAllFilteredByName(name: String?): List<People> {
        val all = findAll()
        return if (name.isNullOrBlank()) all else all.filter { it.firstName.contains(name, true) || it.lastName.contains(name, true) }
    }

    override fun updateOne(id: Long, people: People): People? {
        val existing = peoples[id]
        return if (existing != null) {
            val updated = people.copy(id = id)
            peoples[id] = updated
            updated
        } else {
            null
        }
    }
}