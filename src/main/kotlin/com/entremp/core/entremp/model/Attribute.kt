package com.entremp.core.entremp.model

import java.util.*

data class Attribute(
        val id: UUID,
        val categoryId: UUID,
        val name: String
) {
        companion object {
            fun create(
                    categoryId: UUID,
                    name: String
            ): Attribute {
                    return Attribute(
                            id = UUID.randomUUID(),
                            categoryId = categoryId,
                            name = name
                    )
            }
        }
}