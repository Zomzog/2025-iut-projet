package iut.nantes.project.peoples.repository

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "people")
data class People(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val firstName: String,
    val lastName: String,
    val age: Int,
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(referencedColumnName = "addressId")
    val address: Address,
)

@Entity
@Table(name = "address")
data class Address(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val addressId: Long,
    val street: String,
    val city: String,
    val zipCode: String,
    val country: String
)