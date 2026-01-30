package iut.nantes.project.peoples.controller

import iut.nantes.project.peoples.PeopleService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import iut.nantes.project.peoples.domain.People
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.http.ResponseEntity
import iut.nantes.project.peoples.domain.Address
import org.springframework.web.bind.annotation.DeleteMapping

@RestController
@Validated
class PeopleController(
    private val peopleService: PeopleService
) {
    @GetMapping("/api/v1/peoples")
    fun findAll(
        @RequestParam(required = false)
        name: String?
    ): List<People> {
        return peopleService.findAllFilteredByName(name)
    }

    @GetMapping("/api/v1/peoples/{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<People> {
       return peopleService.findOne(id)?.let { return ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    }

    @PostMapping("/api/v1/peoples")
    fun create(@RequestBody people: People): People {
        return peopleService.create(people)
    }

    @PutMapping("/api/v1/peoples/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody people: People
    ): ResponseEntity<People> {
        val updated = peopleService.update(id, people)
        return if (updated != null) {
            ResponseEntity.ok(updated)
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/api/v1/peoples/{id}/address")
    fun updateAddress(
        @PathVariable id: Long,
        @RequestBody address: Address
    ): ResponseEntity<People> {
        val updated = peopleService.updateAddress(id, address)
        return if (updated != null) {
            ResponseEntity.ok(updated)
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/api/v1/peoples/{id}")
    fun deleteOne(@PathVariable id: Long): ResponseEntity<Void> {
        return if (peopleService.deleteOne(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}