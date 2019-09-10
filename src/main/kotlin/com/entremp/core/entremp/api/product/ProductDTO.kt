package com.entremp.core.entremp.api.product

class ProductDTO(
        val id: String,
        val name: String,
        val min: Int,
        val max: Int,
        val batch: Int,
        val description: String) {

    constructor(): this(
            id = "",
            name = "",
            min = 1,
            max = 1,
            batch = 1,
            description = ""
    )
}