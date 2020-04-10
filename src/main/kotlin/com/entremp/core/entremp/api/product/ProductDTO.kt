package com.entremp.core.entremp.api.product

data class ProductDTO(
        val id: String,
        val name: String,
        val min: Int,
        val max: Int,
        val batch: Int,
        val description: String
)