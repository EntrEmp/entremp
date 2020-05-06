package com.entremp.core.entremp.api.user

data class ResetPasswordDTO(
    val token: String,
    val password: String,
    val confirmation: String
)