package iut.nantes.project.peoples.repository

interface PeopleDatabase {
    fun save(people: People): People

    fun deleteById(id: Long): Long

    fun findById(id: Long): People?

    fun findAll(name: String?): List<People>

    fun findAddress(
        street: String,
        city: String,
        zip: String,
        country: String
    ): Address?
}