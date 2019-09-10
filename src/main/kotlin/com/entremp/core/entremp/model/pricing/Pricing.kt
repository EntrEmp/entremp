package com.entremp.core.entremp.model.pricing

import com.entremp.core.entremp.model.DeliveryTerm
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.model.sample.Sample
import com.entremp.core.entremp.model.product.Product
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class Pricing(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn(name="buyer_id")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val buyer: User? = null,

        @ManyToOne
        @JoinColumn(name="provider_id")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val provider: User? = null,

        @ManyToOne
        @JoinColumn(name="product_id")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val product: Product? = null,

        @OneToMany(mappedBy = "pricing")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val pricingAttachements: List<PricingAttachement> = emptyList(),

        @OneToOne(mappedBy = "pricing")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val pricingSample: Sample? = null,

        @OneToOne(mappedBy = "pricing")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val budget: Budget? = null,

        val quantity: Long,

        val specifications: String,

        val deliveryTerm: DeliveryTerm = DeliveryTerm.IN_15_DAYS,

        val sample: Boolean = false,

        val status: PricingStatus = PricingStatus.PENDING
)