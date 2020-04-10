package com.entremp.core.entremp.api.user

data class EditUserPasswordDTO(
    val current: String,
    val password: String,
    val confirmation: String,

    val role: String
)