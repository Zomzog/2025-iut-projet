package iut.nantes.project.rooms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class RoomsApplication

fun main(args: Array<String>) {
    runApplication<RoomsApplication>(*args)
}

/** This is not an example of a proper application design. It is just for test purposes. */
@RestController
@RequestMapping("/api/v1")
class RoomController {

    val defaultRooms = mutableMapOf(
        1L to Room(1, "Salle A", 10, 8, 18),
        2L to Room(2, "Salle B", 20, 9, 17),
        3L to Room(3, "Salle C", null, 1, 8),
        42L to Room(42, "La salle 42", 1, 7, 20),
        45L to Room(45, "What time?", 1, -7, 28),
    )
    val rooms: MutableMap<Long, Room> = defaultRooms

    @GetMapping("rooms")
    fun listAll(): List<Room> {
        return rooms.values.toList()
    }

    @GetMapping("rooms/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<*> {
        return when (id) {
            666L -> ResponseEntity.internalServerError().body("BAD REQUEST FROM ROOM SERVICE!!!")
            777L -> ResponseEntity.status(400).body("Invalid room ID provided.")
            else -> {
                val room = rooms[id]
                if (room != null) {
                    ResponseEntity.ok(room)
                } else {
                    ResponseEntity.notFound().build()
                }
            }
        }
    }

    @PostMapping("rooms")
    fun create(@RequestBody room: Room): ResponseEntity<Room> {
        val nextId = (rooms.keys.maxOrNull() ?: 0L) + 1L
        val newRoom = room.copy(id = nextId)
        rooms[nextId] = newRoom
        return ResponseEntity.ok(newRoom)
    }

    @PatchMapping("rooms/{id}")
    fun updateName(@PathVariable id: Long, @RequestBody update: UpdateRoomNameRequest): ResponseEntity<Room> {
        val room = rooms[id]
        return if (room != null) {
            val updatedRoom = room.copy(name = update.name)
            rooms[id] = updatedRoom
            ResponseEntity.ok(updatedRoom)
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("rooms/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        return if (rooms.remove(id) != null) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("rooms/reset")
    fun reset(): ResponseEntity<Void> {
        rooms += defaultRooms
        return ResponseEntity.noContent().build()
    }
}

data class Room(val id: Long, val name: String, val capacity: Int?, val startingHours: Int?, val endingHours: Int?)
data class UpdateRoomNameRequest(val name: String)
