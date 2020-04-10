package com.entremp.core.entremp.api.product

data class FullCreateProductDTO(
    val name: String,
    val description: String,

    val stock: Int,
    val batch: Int,
    val minimum: Int,
    val maximum: Int,

    val tags: String,

    val certifications: String,
    val categories: String,
    val attributes: String
)