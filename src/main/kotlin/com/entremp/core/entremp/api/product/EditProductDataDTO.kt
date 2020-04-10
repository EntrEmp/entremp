package com.entremp.core.entremp.api.product

data class EditProductDataDTO(
    val name: String,
    val description: String,
    val categories: String,
    val attributes: String
)