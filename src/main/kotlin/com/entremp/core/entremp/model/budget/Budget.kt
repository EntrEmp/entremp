package com.entremp.core.entremp.model.budget

import com.entremp.core.entremp.model.DeliveryTerm
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.review.Review
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class Budget(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @OneToOne
        @JoinColumn(name="pricing_id")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val pricing: Pricing? = null,

        @OneToOne(mappedBy = "budget")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val review: Review? = null,

        val price: Double,
        val iva: Double,

        val deliveryTerm: DeliveryTerm = DeliveryTerm.IN_15_DAYS,

        val deliveryConditions: String,
        val paymentConditions: String,
        val specifications: String,

        @OneToMany(mappedBy = "budget")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val budgetAttachement: List<BudgetAttachement> = emptyList()
)