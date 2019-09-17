package com.entremp.core.entremp.persistence.pricing

import com.entremp.core.entremp.model.DeliveryTerm
import com.entremp.core.entremp.model.pricing.PricingStatus
import com.entremp.core.entremp.persistence.pricing.PricingAttachements.nullable
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object Pricings:  UUIDTable(name = "pricings") {
    val buyerId: Column<UUID> = uuid(name = "buyer_id")
    val providerId: Column<UUID> = uuid(name = "provider_id")
    val productId: Column<UUID> = uuid(name = "product_id")
    val quantity: Column<Long> = long(name = "quantity")
    val specifications: Column<String> = varchar(
            name = "specifications",
            length = 255
    )
    val sample: Column<Boolean> = bool(name = "sample")
    val deliveryTerm: Column<DeliveryTerm> = enumerationByName(
            name = "delivery_term",
            length = 10,
            klass = DeliveryTerm::class
    )
    val status: Column<PricingStatus> = enumerationByName(
            name = "pricing_status",
            length = 20,
            klass = PricingStatus::class
    )
    val createdAt: Column<DateTime> = PricingAttachements.datetime(name = "created_at")
    val updatedAt: Column<DateTime?> = PricingAttachements.date(name = "updated_at").nullable()

}