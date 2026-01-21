package iut.nantes.project.peoples.repository

class HashPeopleDatabase : PeopleDatabase {
    private val peoples = mutableMapOf<Long, People>()
    private val addresses = mutableMapOf<Long, Address>()

    private var peopleIdGenerator:Long=0
    private var addressIdGenerator:Long=0

    override fun save(people: People): People {
        val addressToSave = people.address
        val finalAddress = if (addressToSave.addressId == 0L) {
            val newAddrId = addressIdGenerator
            addressIdGenerator++
            val savedAddr = addressToSave.copy(addressId = newAddrId)
            addresses[newAddrId] = savedAddr
            savedAddr
        } else {
            addressToSave
        }

        val newId = if (people.id == 0L) peopleIdGenerator else people.id
        peopleIdGenerator++

        val savedPeople = people.copy(id = newId, address = finalAddress)
        peoples[newId] = savedPeople

        return savedPeople
    }

    override fun deleteById(id: Long) :Long {
        val peoplesTemp = peoples
        peoples.remove(id)
        return (peoplesTemp.size-peoples.size).toLong()
    }

    override fun findById(id: Long): People? {
        return peoples[id]
    }

    override fun findAll(name: String?): List<People> {
        return if (name == null) {
            peoples.values.toList()
        } else {
            peoples.values.filter { it.lastName == name }
        }
    }

    override fun findAddress(street: String, city: String, zip: String, country: String): Address? {
        return addresses.values.find {
            it.street == street && it.city == city && it.zipCode == zip && it.country == country
        }
    }
}