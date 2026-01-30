package iut.nantes.project.reservations

import iut.nantes.project.reservations.domain.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID


import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate

interface ReservationRepository : JpaRepository<ReservationEntity, UUID> {

    fun findByRoomIdIn(roomIds: List<Long>): List<ReservationEntity>

    fun deleteAllByOwnerId(ownerId: Long)
}

@Entity
@Table(name ="reservations")
data class ReservationEntity(
    @Id
    val id: UUID,
    val ownerId: Long,
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_id")
    val peoples: List<PeopleEntity> = emptyList(),
    val roomId: Long = 0,
    val start: Long = 0,
    @Column(name=" endHour")
    val end: Long = 0,
    val reservationDay: LocalDate = LocalDate.now()
)

@Entity
@Table(name ="peoples")
data class PeopleEntity(
    @Id
    val id: Long,
)
