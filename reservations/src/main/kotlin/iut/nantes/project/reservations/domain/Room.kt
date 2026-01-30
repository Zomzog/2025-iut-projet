package iut.nantes.project.reservations.domain

data class Room(val id: Long, val name: String, val capacity: Int?, val startingHours: Int?, val endingHours: Int?)
