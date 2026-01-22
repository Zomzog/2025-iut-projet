package iut.nantes.project.peoples.config

import iut.nantes.project.peoples.controller.PeopleController
import iut.nantes.project.peoples.repository.AddressRepository
import iut.nantes.project.peoples.repository.HashPeopleDatabase
import iut.nantes.project.peoples.repository.JpaPeopleDatabase
import iut.nantes.project.peoples.repository.PeopleDatabase
import iut.nantes.project.peoples.repository.PeopleRepository
import iut.nantes.project.peoples.service.PeopleService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class PeopleConfig {

    @Bean
    @Profile("dev")
    fun devDatabase(): PeopleDatabase {
        return HashPeopleDatabase()
    }

    @Bean
    @Profile("!dev")
    fun database(
        peopleRepository: PeopleRepository,
        addressRepository: AddressRepository
    ): PeopleDatabase {
        return JpaPeopleDatabase(peopleRepository, addressRepository)
    }

    @Bean
    fun peopleService(database: PeopleDatabase)= PeopleService(database)

    @Bean
    fun peopleController(peopleService: PeopleService) = PeopleController(peopleService)

}