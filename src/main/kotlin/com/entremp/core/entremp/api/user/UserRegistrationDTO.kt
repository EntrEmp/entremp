package com.entremp.core.entremp.api.user

data class UserRegistrationDTO(
    val name: String,
    val email: String,
    val password: String,
    val confirmation: String,
    val phone: Long,
    val cuit: Long
)