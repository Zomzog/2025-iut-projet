package iut.nantes.project.peoples

import iut.nantes.project.peoples.domain.Address
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import iut.nantes.project.peoples.domain.People
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Service
class PeopleService @Autowired constructor(
    private val db: InMemoryDatabase
) {
    private val reservationUrl = "http://localhost:8082/api/v1/reservations" // adjust as needed
    private val webClient = WebClient.create()

    fun findAll(): List<People> = db.findAll()
    fun findAllFilteredByName(name: String?): List<People> = db.findAllFilteredByName(name)
    fun findOne(id: Long): People? = db.findOne(id)
    fun create(people: People): People {
        return db.saveOneWithGeneratedId(people)
    }
    fun update(id: Long, people: People): People? = db.updateOne(id, people)
    fun updateAddress(id: Long, address: Address): People? {
        val existing = db.findOne(id)
        return if (existing != null) {
            val updated = existing.copy(address = address)
            db.saveOne(updated)
            updated
        } else {
            null
        }
    }
    fun deleteOne(id: Long): Boolean {
        webClient.delete().uri("$reservationUrl?ownerId=$id")
            .retrieve()
            .onStatus({ it.isError }, { Mono.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Delete failed ${it.statusCode()}"))})
            .bodyToMono(Unit::class.java)
            .block()
       return db.deleteOne(id)
    }
}