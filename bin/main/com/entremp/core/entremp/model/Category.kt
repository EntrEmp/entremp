package com.entremp.core.entremp.model

import com.entremp.core.entremp.model.product.ProductCategory
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class Category(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        val name: String,

        @OneToMany(mappedBy = "category")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val attributes: List<Attribute> = emptyList(),

        @OneToMany(mappedBy = "category")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val productCategories: List<ProductCategory> = emptyList()
)