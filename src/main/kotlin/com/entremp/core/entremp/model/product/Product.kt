package com.entremp.core.entremp.model.product

import com.entremp.core.entremp.model.Category
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.user.User
import com.fasterxml.jackson.annotation.*
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class Product(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn(name="user_id")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val user: User? = null,

        val name: String,
        val minimum: Int,
        val maximum: Int,
        val batchSize: Int,

        @Column(length = 1000)
        val description: String,

        @OneToMany(mappedBy = "product")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val productCategories: List<ProductCategory> = emptyList(),

        @OneToMany(mappedBy = "product")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val productAttributes: List<ProductAttribute> = emptyList(),

        @OneToMany(mappedBy = "product")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val images: List<ProductImage> = emptyList(),

        @OneToMany(mappedBy = "product")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val pricings: List<Pricing> = emptyList()
) {
    @JsonProperty
    fun categories(): List<Category> = productCategories.mapNotNull { item ->
        item.category
    }

    @JsonProperty
    fun attributes(): List<String> = productAttributes.mapNotNull { item ->
        item.attribute?.name
    }

    fun mainImage(): String {
        return images.firstOrNull()?.s3Link() ?: ""
    }

    fun userName(): String {
        return user?.name ?: ""
    }
}