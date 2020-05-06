package com.entremp.core.entremp.model.pricing

import com.entremp.core.entremp.model.Fileable
import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class PricingAttachement(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val pricing: Pricing? = null,

        private val fileLocation: String
): Fileable(fileLocation){
        fun extension(): String =
                fileLocation
                        ?.let {it ->
                                it.substringAfterLast(".")
                        }
                        ?: "jpg"

        fun filename(): String = "$id.${extension()}"

        fun s3Link(): String = "https://entremp.s3-sa-east-1.amazonaws.com/${filename()}"
}