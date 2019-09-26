package com.entremp.core.entremp.model.pricing

import com.entremp.core.entremp.model.commons.Fileable
import org.joda.time.DateTime
import java.util.*

data class PricingAttachement(
        val id: UUID,
        val pricingId: UUID,
        val fileLocation: String,
        val createdAt: DateTime,
        val updatedAt: DateTime?
): Fileable(fileLocation) {
        companion object {
            fun create(
                    pricingId: UUID,
                    fileLocation: String
            ): PricingAttachement {
                    return PricingAttachement(
                            id = UUID.randomUUID(),
                            pricingId = pricingId,
                            fileLocation = fileLocation,
                            createdAt = DateTime.now(),
                            updatedAt = null
                    )
            }
        }
}