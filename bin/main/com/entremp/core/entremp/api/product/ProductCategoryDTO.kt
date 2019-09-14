package com.entremp.core.entremp.api.product

import com.fasterxml.jackson.annotation.JsonProperty

class ProductCategoryDTO(
        val categories: Set<String>
) {
    constructor(): this(
            categories = emptySet()
    )
}