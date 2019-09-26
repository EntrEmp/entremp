package com.entremp.core.entremp.persistence.sample

import com.entremp.core.entremp.model.sample.Sample
import com.entremp.core.entremp.model.sample.SampleStatus
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import java.util.*

class SampleEntity(id: EntityID<UUID>)
    : AbstractEntity<Sample>(id) {

    companion object: AbstractEntityClass<Sample, SampleEntity>(
        SampleTable
    )

    var pricingId: UUID by SampleTable.pricingId
    var sent: Boolean by SampleTable.sent
    var received: Boolean by SampleTable.received
    var status: SampleStatus by SampleTable.status

    override fun create(source: Sample): AbstractEntity<Sample> {
        return update(source)
    }

    override fun update(source: Sample): AbstractEntity<Sample> {
        pricingId = source.pricingId
        sent = source.sent
        received = source.received
        status = source.status

        return this
    }

    override fun toDomainType(): Sample {
        return Sample(
            id = id.value,
            pricingId = pricingId,
            sent = sent,
            received = received,
            status = status
        )
    }
}