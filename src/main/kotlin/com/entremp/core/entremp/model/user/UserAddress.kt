package com.entremp.core.entremp.model.user

import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class UserAddress(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    val id: String? = null,

    @ManyToOne
    @JoinColumn
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    val user: User? = null,

    val address: String,

    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,

    val locality: String? = "",
    val state: String? = "",

    val googleAddress: String? = ""
) {
    fun wrapper(): String = "$locality, $state"
}