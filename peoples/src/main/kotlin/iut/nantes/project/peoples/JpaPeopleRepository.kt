package iut.nantes.project.peoples

import iut.nantes.project.peoples.domain.People
import iut.nantes.project.peoples.domain.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Profile("prod")
@Repository
interface JpaPeopleRepositorySpring : JpaRepository<People, Long> {
    @Query("SELECT p FROM People p WHERE (:name IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%')))")
    fun findAllFilteredByName(@Param("name") name: String?): List<People>

    @Query("SELECT p.address FROM People p WHERE p.address.city = :city")
    fun findAddressByCity(@Param("city") city: String): List<Address>
}

@Profile("prod")
class JpaPeopleRepository(private val springRepo: JpaPeopleRepositorySpring) : PeopleRepository {
    override fun findAll(): List<People> = springRepo.findAll()
    override fun findOne(id: Long): People? = springRepo.findById(id).orElse(null)
    override fun saveOne(people: People): People = springRepo.save(people)
    override fun saveOneWithGeneratedId(people: People): People = springRepo.save(people.copy(id = 0L))
    override fun deleteOne(id: Long): Boolean {
        if (!springRepo.existsById(id)) return false
        springRepo.deleteById(id)
        return true
    }
    override fun findAddressByCity(city: String): List<Address> = springRepo.findAddressByCity(city)
    override fun findAllFilteredByName(name: String?): List<People> = springRepo.findAllFilteredByName(name)
    override fun updateOne(id: Long, people: People): People? {
        val existing = findOne(id) ?: return null
        val updated = people.copy(id = id)
        return saveOne(updated)
    }
}
