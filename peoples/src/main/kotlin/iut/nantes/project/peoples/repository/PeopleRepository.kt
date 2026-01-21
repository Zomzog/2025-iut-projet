package iut.nantes.project.peoples.repository

import org.springframework.data.jpa.repository.JpaRepository

interface PeopleRepository : JpaRepository<People, Long> {
    fun removeById(id: Long): Long
    fun findPeopleByLastName(lastName: String): List<People>
}