package com.entremp.core.entremp.persistence.pricing


import com.entremp.core.entremp.model.pricing.PricingAttachement
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class PricingAttachmentDAO: TransactionSupport() {

    fun all(): List<PricingAttachement> = transaction {
        PricingAttachmentEntity
                .all()
                .map { entity: PricingAttachmentEntity ->
                    entity.toDomainType()
                }
    }

    fun findById(id: UUID): PricingAttachement = transaction {
        PricingAttachmentEntity[id].toDomainType()
    }


    fun saveOrUpdate(source: PricingAttachement): PricingAttachement = transaction {
        PricingAttachmentEntity.saveOrUpdate(
                id = source.id,
                source = source
        )
    }

    fun delete(id: UUID) = transaction {
        PricingAttachmentEntity[id].delete()
    }
}