package iut.nantes.project.reservations

import iut.nantes.project.reservations.domain.Reservation
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID

@RestController
@RequestMapping("/api/v1/reservations")
@Validated
class ReservationController @Autowired constructor(
    private val reservationService: ReservationService
) {
    @GetMapping
    fun listAll(@RequestParam(required = false) roomIds: List<Long>?, @RequestParam(required = false) ownerId: Long?): List<Reservation> {
        return reservationService.listAll(roomIds, ownerId)
    }

    @PostMapping
    fun create(@Valid @RequestBody reservation: Reservation): ResponseEntity<Reservation> {
        val created = reservationService.createReservation(reservation)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping("/{reservationId}")
    fun get(@PathVariable reservationId: UUID): ResponseEntity<Reservation> {
        return reservationService.findOne(reservationId)?.let {
             ResponseEntity.ok(it)
        } ?:  ResponseEntity.notFound().build()
    }

    @DeleteMapping
    fun deleteByOwnerId(
        @RequestParam ownerId: Long
    ): ResponseEntity<Void> {
         reservationService.deleteByOwnerId(ownerId)
            return ResponseEntity.noContent().build()
    }
}
