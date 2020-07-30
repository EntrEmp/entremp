package com.entremp.core.entremp.api.support

data class ContactDTO(
    val name: String,
    val email: String,
    val phone: Long,
    val cuit: Long,
    val seller: Boolean,
    val criteria: String
) {
}