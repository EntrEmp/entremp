package com.entremp.core.entremp.api.user

data class EditUserDataDTO(
    val name: String,
    val phone: Long,
    val cuit: Long,

    val role: String
)