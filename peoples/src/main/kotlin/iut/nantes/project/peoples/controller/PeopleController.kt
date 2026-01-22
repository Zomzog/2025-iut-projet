package iut.nantes.project.peoples.controller

import iut.nantes.project.peoples.repository.Address
import iut.nantes.project.peoples.repository.People
import iut.nantes.project.peoples.repository.PeopleDTO
import iut.nantes.project.peoples.service.PeopleService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
class PeopleController(private val peopleService: PeopleService) {
    @PostMapping("/api/v1/peoples")
    fun createPeople(@Valid @RequestBody peopleDTO: PeopleDTO): ResponseEntity<People> {
        val peopleEntity = dtoToEntity(peopleDTO)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(peopleService.createPeople(peopleEntity))
    }

    @GetMapping("/api/v1/peoples")
    fun findAll(@RequestParam(required = false) name: String?) : List<People> {
        return peopleService.findPeople(name)
    }

    @GetMapping("/api/v1/peoples/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<People> {
        val people = peopleService.findPeopleById(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(people
            )
    }

    @PutMapping("/api/v1/peoples/{id}")
    fun updatePeople(
        @PathVariable id: Long,
        @Valid @RequestBody peopleDTO: PeopleDTO
    ): ResponseEntity<People> {
        val peopleEntity = dtoToEntity(peopleDTO)
        val updated = peopleService.updatePeople(id, peopleEntity)
        return if (updated != null) {
            ResponseEntity.ok(updated)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/api/v1/peoples/{id}")
    fun deletePeople(@PathVariable id: Long): ResponseEntity<People> {
        return if (peopleService.deletePeople(id)){
            ResponseEntity.status(204)
                .build()
        } else ResponseEntity.notFound().build()
    }

    private fun dtoToEntity(dto: PeopleDTO): People {
        val address = Address(
            addressId = 0,
            street = dto.address.street,
            city = dto.address.city,
            zipCode = dto.address.zipCode,
            country = dto.address.country
        )
        return People(0,
            dto.firstName,
            dto.lastName,
            dto.age,
            address
        )
    }

}