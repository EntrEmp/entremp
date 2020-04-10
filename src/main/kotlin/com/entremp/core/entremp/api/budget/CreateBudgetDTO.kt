package com.entremp.core.entremp.api.budget

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

data class CreateBudgetDTO(
    val pricingId: String,
    val price: Double,
    val quantity: Long,
    val delivery: Long,
    val ttl: Int,
    val total: Double,
    val billing: String
) {
    fun iva(): Double {
        return if(billing == "FINAL_CONSUMER"){
            total * 0.21
        } else {
            0.0
        }
    }

    fun ttlDateTime(): DateTime = DateTime.now(DateTimeZone.UTC).plusDays(ttl)
}