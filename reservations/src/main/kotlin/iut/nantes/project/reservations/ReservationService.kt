package iut.nantes.project.reservations

import iut.nantes.project.reservations.domain.People
import iut.nantes.project.reservations.domain.Reservation
import iut.nantes.project.reservations.domain.Room
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class ReservationService @Autowired constructor(
    private val reservationRepository: ReservationRepository
) {
    private val roomServiceUrl = "http://localhost:8083/api/v1/rooms/" // adjust as needed
    private val peopleServiceUrl = "http://localhost:8081/api/v1/peoples/" // adjust as needed
    private val webClient = RestClient.create()

    fun createReservation(reservation: Reservation): Reservation {
        // Check room exists
        val toSave = reservation.copy(id = UUID.randomUUID())
        webClient.get().uri("$roomServiceUrl${toSave.roomId}")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, rep_ -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Room not found") }
            .body(Room::class.java)
        // Check all peoples exist
        toSave.peoples.forEach { peopleId ->
            webClient.get().uri("$peopleServiceUrl$peopleId")
                .retrieve()
                .onStatus(HttpStatusCode::isError) { _, rep -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "People $peopleId not found") }
                .body(People::class.java)
        }
        return reservationRepository.save(toSave.toEntity()).toReservation()
    }

    fun listAll(roomIds: List<Long>?, ownerId: Long?): List<Reservation> {
        val reservations = if (roomIds.isNullOrEmpty()) {
            reservationRepository.findAll()
        } else {
            reservationRepository.findByRoomIdIn(roomIds)
        }
        val filtered = if (ownerId != null) {
            reservations.filter { it.ownerId == ownerId }
        } else {
            reservations
        }
        return filtered.map { it.toReservation() }
    }

    @Transactional
    fun deleteByOwnerId(ownerId: Long) {
        reservationRepository.deleteAllByOwnerId(ownerId)
    }

    fun findOne(reservationId: UUID): Reservation? {
        return reservationRepository.findById(reservationId)
            .map { it.toReservation() }
            .orElse(null)
    }
}

fun Reservation.toEntity(): ReservationEntity {
    return ReservationEntity(
        id = this.id,
        ownerId = this.ownerId,
        peoples = this.peoples.map { PeopleEntity(it) },
        roomId = this.roomId,
        start = this.start,
        end = this.end,
        reservationDay = this.day
    )
}

fun ReservationEntity.toReservation(): Reservation = Reservation(
    id = this.id,
    ownerId = this.ownerId,
    peoples = this.peoples.map { it.id },
    roomId = this.roomId,
    start = this.start,
    end = this.end,
    day = this.reservationDay
)
