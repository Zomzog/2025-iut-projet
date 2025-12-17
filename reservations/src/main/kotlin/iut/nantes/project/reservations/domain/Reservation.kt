package iut.nantes.project.reservations.domain

import java.time.LocalDate
import java.util.*

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