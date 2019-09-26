package com.entremp.core.entremp.persistence.sample

import com.entremp.core.entremp.model.sample.SampleStatus
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object SampleTable: UUIDTable(name = "sample") {
    val pricingId: Column<UUID> = uuid("pricing_id")
    val sent: Column<Boolean> = bool("sent")
    val received: Column<Boolean> = bool("received")
    val status: Column<SampleStatus> = enumerationByName(
        name = "status",
        length = 8,
        klass = SampleStatus::class
    )
}