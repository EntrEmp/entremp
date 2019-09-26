package com.entremp.core.entremp.persistence.sample

import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.sample.Sample
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class SampleDAO: TransactionSupport() {

    fun all(): List<Sample> = transaction {
        SampleEntity
            .all()
            .map { entity: SampleEntity ->
                entity.toDomainType()
            }
    }

    fun findById(id: UUID): Sample = transaction {
        SampleEntity[id].toDomainType()
    }

    fun saveOrUpdate(source: Sample): Sample = transaction {
        SampleEntity.saveOrUpdate(
            id = source.id,
            source = source
        )
    }

    fun delete(id: UUID) = transaction {
        SampleEntity[id].delete()
    }

    fun deleteByPricing(source: Pricing) = transaction {
        SampleEntity
            .find {
                SampleTable.pricingId eq source.id
            }
            .map { entity ->
                entity.delete()
            }
    }
}