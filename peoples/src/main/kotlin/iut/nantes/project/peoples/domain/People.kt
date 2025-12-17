package iut.nantes.project.peoples.domain


data class People(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val address: Address,
    )

data class Address(
    val street: String,
    val city: String,
    val zipCode: String,
    val country: String
)