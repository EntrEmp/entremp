package com.entremp.core.entremp.persistence.pricing

import com.entremp.core.entremp.model.commons.DeliveryTerm
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.pricing.PricingStatus
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class PricingEntity(id: EntityID<UUID>)
    : AbstractEntity<Pricing>(id){

    companion object: AbstractEntityClass<Pricing, PricingEntity> (
            PricingTable
    )

    var buyerId: UUID by PricingTable.buyerId
    var providerId: UUID by PricingTable.providerId
    var productId: UUID by PricingTable.productId
    var quantity: Long by PricingTable.quantity
    var specifications: String by PricingTable.specifications
    var sample: Boolean by PricingTable.sample
    var deliveryTerm: DeliveryTerm by PricingTable.deliveryTerm
    var status: PricingStatus by PricingTable.status
    var createdAt: DateTime by PricingTable.createdAt
    var updatedAt: DateTime? by PricingTable.updatedAt


    override fun create(source: Pricing): AbstractEntity<Pricing> {
        return update(source)
    }

    override fun update(source: Pricing): AbstractEntity<Pricing> {
        buyerId = source.buyerId
        providerId = source.providerId
        productId = source.productId
        quantity = source.quantity
        specifications = source.specifications
        sample = source.sample
        deliveryTerm = source.deliveryTerm
        status = source.status
        createdAt = source.createdAt
        updatedAt = source.updatedAt

        return this
    }

    override fun toDomainType(): Pricing {
        return Pricing(
                id = id.value,
                buyerId = buyerId,
                providerId = providerId,
                productId = productId,
                quantity = quantity,
                specifications = specifications,
                sample = sample,
                deliveryTerm = deliveryTerm,
                status = status,
                createdAt = createdAt,
                updatedAt = updatedAt
        )
    }
}