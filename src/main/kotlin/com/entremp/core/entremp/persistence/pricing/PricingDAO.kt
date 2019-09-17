package com.entremp.core.entremp.persistence.pricing

import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class PricingDAO: TransactionSupport() {

    fun all(): List<Pricing> = transaction {
        PricingEntity
                .all()
                .map { entity: PricingEntity ->
                    entity.toDomainType()
                }
    }

    fun findById(id: UUID): Pricing = transaction {
        PricingEntity[id].toDomainType()
    }


    fun saveOrUpdate(source: Pricing): Pricing = transaction {
        PricingEntity.saveOrUpdate(
                id = source.id,
                source = source
        )
    }

    fun delete(id: UUID) = transaction {
        PricingEntity[id].delete()
    }
}