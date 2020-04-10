package com.entremp.core.entremp.model.product

import com.entremp.core.entremp.model.Fileable
import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class ProductImage(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val product: Product? = null,

        val fileLocation: String?
): Fileable(fileLocation) {

        fun s3Link(): String = "https://entremp.s3-sa-east-1.amazonaws.com/$id.jpg"

}