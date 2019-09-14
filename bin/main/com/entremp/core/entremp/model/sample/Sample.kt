package com.entremp.core.entremp.model.sample

import com.entremp.core.entremp.model.pricing.Pricing
import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

//TODO agregar un adjunto con el "Remito" asociado a la Muestra
@Entity
@EqualsAndHashCode
data class Sample(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @OneToOne
        @JoinColumn(name = "pricing_id")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val pricing: Pricing? = null,

        val sent: Boolean = false,

        val received: Boolean = false,

        val status: SampleStatus = SampleStatus.PENDING
)