package iut.nantes.project.peoples.repository

class JpaPeopleDatabase(
    private val peopleRepository: PeopleRepository,
    private val addressRepository: AddressRepository
) : PeopleDatabase {

    override fun save(people: People): People = peopleRepository.save(people)

    override fun deleteById(id: Long): Long = peopleRepository.removeById(id)

    override fun findById(id: Long): People? = peopleRepository.findById(id).orElse(null)

    override fun findAll(name: String?): List<People> {
        return if (name == null) peopleRepository.findAll()
        else peopleRepository.findPeopleByLastName(name)
    }

    override fun findAddress(
        street: String,
        city: String,
        zip: String,
        country: String
    ): Address? {
        return addressRepository.findByStreetAndCityAndZipCodeAndCountry(
            street,
            city,
            zip,
            country
        )
    }
}