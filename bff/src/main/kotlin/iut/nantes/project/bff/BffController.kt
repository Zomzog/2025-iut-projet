package iut.nantes.project.bff

import org.jetbrains.annotations.NotNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient
import java.time.LocalDate
import java.util.UUID

@RestController
class BffController {

        private val roomServiceUrl = "http://localhost:8083/api/v1/rooms/" // adjust as needed
        private val peopleServiceUrl = "http://localhost:8081/api/v1/peoples/" // adjust as needed
    private val reservationUrl = "http://localhost:8082/api/v1/reservations" // adjust as needed

        private val webClient = RestClient.create()

    @GetMapping("/peoples/{id}")
    fun getPeoples(@PathVariable id: String): BffPeople {
        val people = webClient.get().uri("$peopleServiceUrl${id}")
            .retrieve()
            .body(People::class.java) ?: throw Exception("People not found")
       val reservations = webClient.get().uri("$reservationUrl?ownerId=${id}")
            .retrieve()
            .body(Array<Reservation>::class.java)?.toList() ?: emptyList()
        val bffPeople = BffPeople(
            firstName = people.firstName,
            lastName = people.lastName,
            age = people.age,
            address = people.address,
            reservations = reservations.map { it.id }
        )
        return bffPeople
    }

    @GetMapping("/reservations/{id}")
    fun getReservations(@PathVariable id: UUID): FullResa {
        val reservation = webClient.get().uri("$reservationUrl/${id}")
            .retrieve()
            .body(Reservation::class.java) ?: throw Exception("Reservation not found")
        val owner  = webClient.get().uri("$peopleServiceUrl${reservation.ownerId}")
            .retrieve()
            .body(People::class.java).toBff(reservation.ownerId)
        val peoples = reservation.peoples.map { p ->
            webClient.get().uri("$peopleServiceUrl${p}")
                .retrieve()
                .body(People::class.java).toBff(p)
        }
        val room = webClient.get().uri("$roomServiceUrl${reservation.roomId}")
            .retrieve()
            .body(Room::class.java) ?: throw Exception("Room not found")
        return FullResa(
            id = reservation.id,
            owner = owner,
            peoples = peoples,
            roomId = room,
            start = reservation.start,
            end = reservation.end,
            day = reservation.day
        )
    }

}

fun People?.toBff(id: Long): BffSubPeople {
    return if (this == null) {defaultSub(id)} else {
        BffSubPeople(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
        )
    }
}

fun defaultSub(id: Long) = BffSubPeople(id, "DELETED", "DELETED")

data class FullResa(
    val id: UUID,
    val owner: BffSubPeople,
    val peoples: List<BffSubPeople>,
    val roomId: Room,
    val start: Long,
    val end: Long,
    val day: LocalDate
)

data class Room(val id: Long, val name: String, val capacity: Int?, val startingHours: Int?, val endingHours: Int?)

data class BffSubPeople(
    val id: Long,
    val firstName: String,
    val lastName: String,

)
data class BffPeople(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val address: Address,
    val reservations: List<UUID>
)

data class People(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val address: Address,
)


data class Address(
    val street: String,
    val city: String,
    val zipCode: String,
    val country: String
)

data class Reservation(
    val id: UUID,
    val ownerId: Long,
    val peoples: List<Long> = emptyList(),
    val roomId: Long = 0,
    val start: Long = 0,
    val end: Long = 0,
    val day: LocalDate = LocalDate.now()
)

data class Peoples(
    val id: Long,
)
