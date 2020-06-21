package com.entremp.core.entremp.api.budget

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

data class EditBudgetDTO(
    val price: Double,
    val quantity: Long,
    val delivery: Long,
    val ttl: Int,
    val total: Double,
    val selectedBilling: String,
    val iva: Double
) {
    fun ttlDateTime(): DateTime = DateTime.now(DateTimeZone.UTC).plusDays(ttl)

    fun billing(): String =
        if(selectedBilling == "TYPE_B"){
            "FINAL_CONSUMER"
        } else {
            "ENROLLED"
        }
}