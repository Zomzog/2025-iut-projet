package iut.nantes.project.peoples.repository

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class PeopleDTO(
    @field:NotEmpty
    @field:Size(min = 2, max = 20)
    val firstName: String,
    @field:NotEmpty
    @field:Size(min = 2, max = 50)
    val lastName: String,
    @field:Min(value = 18)
    @field:Max(value = 119)
    val age: Int,
    @field:Valid
    val address: AddressDTO
)

data class AddressDTO(
    @field:Size(min = 5, max = 100)
    val street: String,
    @field:Size(min = 2, max = 50)
    val city: String,
    @field:Pattern(regexp = "^\\d{5}$")
    val zipCode: String,
    val country: String
)