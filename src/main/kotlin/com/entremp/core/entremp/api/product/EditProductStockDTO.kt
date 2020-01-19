package com.entremp.core.entremp.api.product

data class EditProductStockDTO(
    val stock: Int,
    val batch: Int,
    val minimum: Int,
    val maximum: Int
)