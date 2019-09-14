package com.entremp.core.entremp.model.product

import com.entremp.core.entremp.model.Attribute
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class ProductAttribute(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val product: Product? = null,

        @ManyToOne
        @JoinColumn
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val attribute: Attribute? = null,

        val active: Boolean = true
)