package iut.nantes.project.peoples.repository

import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepository : JpaRepository<Address, Long> {
    fun findByStreetAndCityAndZipCodeAndCountry(
        street: String,
        city: String,
        zipCode: String,
        country: String
    ): Address?
}