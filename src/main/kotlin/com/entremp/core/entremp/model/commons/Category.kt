package com.entremp.core.entremp.model.commons

import java.util.*

data class Category(
        val id: UUID,
        val name: String
) {
        companion object {
            fun create(
                    name: String
            ): Category {
                    return Category(
                        id = UUID.randomUUID(),
                        name = name
                    )
            }
        }
}