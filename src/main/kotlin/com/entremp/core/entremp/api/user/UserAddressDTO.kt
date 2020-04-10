package com.entremp.core.entremp.api.user

data class UserAddressDTO(
    val country: String,
    val state: String,
    val town: String,
    val street: String,
    val floor: String,

    val cp: Int,
    val number: Int,

    val contact: String,

    val role: String
)