package com.entremp.core.entremp.model.certification

import com.entremp.core.entremp.model.Fileable
import org.joda.time.DateTime
import java.util.*

data class Certification(
        val id: UUID,
        val userId: UUID,
        val name: String,
        val fileLocation: String,
        val createdAt: DateTime,
        val updatedAt: DateTime?
): Fileable(fileLocation) {
        companion object {
            fun create(
                    userId: UUID,
                    name: String,
                    fileLocation: String
            ): Certification {
                    return Certification(
                            id = UUID.randomUUID(),
                            userId = userId,
                            name = name,
                            fileLocation = fileLocation,
                            createdAt = DateTime.now(),
                            updatedAt = null
                    )
            }
        }
}