package com.entremp.core.entremp.model

import com.entremp.core.entremp.model.product.ProductAttribute
import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class Attribute(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn(name="category_id")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val category: Category? = null,

        @OneToMany(mappedBy = "attribute")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val productAttributes: List<ProductAttribute> = emptyList(),

        val name: String
)