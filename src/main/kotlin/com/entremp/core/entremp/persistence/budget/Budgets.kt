package com.entremp.core.entremp.persistence.budget

import com.entremp.core.entremp.model.DeliveryTerm
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object Budgets: UUIDTable(name = "budgets") {
    val pricingId: Column<UUID> = uuid(name = "pricing_id")
    val price: Column<Double> = double(name = "price")
    val iva: Column<Double> = double(name = "iva")
    val deliveryConditions: Column<String> = varchar(
            name = "delivery_condiitons",
            length = 255
    )
    val paymentConditions: Column<String> = varchar(
            name = "payment_condiitons",
            length = 255
    )
    val specifications: Column<String> = varchar(
            name = "specifications",
            length = 255
    )
    val deliveryTerm: Column<DeliveryTerm> = enumerationByName(
            name = "delivery_term",
            length = 10,
            klass = DeliveryTerm::class
    )
    val createdAt: Column<DateTime> = BudgetAttachements.datetime(name = "created_at")
    val updatedAt: Column<DateTime?> = BudgetAttachements.date(name = "updated_at").nullable()
}