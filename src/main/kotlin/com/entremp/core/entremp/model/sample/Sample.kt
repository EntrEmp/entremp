package com.entremp.core.entremp.model.sample

import java.util.*

//TODO agregar un adjunto con el "Remito" asociado a la Muestra
data class Sample(
        val id: UUID,
        val pricingId: UUID,
        val sent: Boolean,
        val received: Boolean,
        val status: SampleStatus
) {
        companion object {
            fun create(
                    pricingId: UUID
            ): Sample {
                    return Sample(
                            id = UUID.randomUUID(),
                            pricingId = pricingId,
                            sent = false,
                            received = false,
                            status = SampleStatus.PENDING
                    )
            }
        }
}