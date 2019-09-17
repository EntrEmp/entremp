package com.entremp.core.entremp.persistence.pricing

import com.entremp.core.entremp.model.pricing.PricingAttachement
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class PricingAttachmentEntity(id: EntityID<UUID>)
    : AbstractEntity<PricingAttachement>(id){

    companion object: AbstractEntityClass<PricingAttachement, PricingAttachmentEntity>(
            PricingAttachements
    )

    var fileLocation: String by PricingAttachements.fileLocation
    var pricingId: UUID by PricingAttachements.pricingId
    var createdAt: DateTime by PricingAttachements.createdAt
    var updatedAt: DateTime? by PricingAttachements.updatedAt

    override fun create(source: PricingAttachement): AbstractEntity<PricingAttachement> {
        return update(source)
    }

    override fun update(source: PricingAttachement): AbstractEntity<PricingAttachement> {
        fileLocation = source.fileLocation
        pricingId = source.pricingId
        createdAt = source.createdAt
        updatedAt = source.updatedAt

        return this
    }

    override fun toDomainType(): PricingAttachement {
        return PricingAttachement(
                id = id.value,
                pricingId = pricingId,
                fileLocation = fileLocation,
                createdAt = createdAt,
                updatedAt = updatedAt
        )
    }
}