package com.entremp.core.entremp.model.budget

import com.entremp.core.entremp.model.chat.Chat
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.review.Review
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
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
        val quantity: Long,

        val iva: Double,

        val total: Double,

        val billing: String,

        val selectedBilling: String? = null,

        val ttl: DateTime,
        val deliveryAfterConfirm: Long,

        val confirmationDate: DateTime? = null,
        val deliveryDate: DateTime? = null,


        @OneToMany(mappedBy = "budget")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val budgetAttachement: List<BudgetAttachement> = emptyList(),

        @OneToOne
        @JoinTable(
                name = "budget_chat",
                joinColumns = [JoinColumn(name = "budget_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "chat_id", referencedColumnName = "id")]
        )
        @EqualsAndHashCode.Exclude
        val chat: Chat? = null
){
        fun ttlDate(): String = ttl.toString("MM/dd/yyyy")

        fun expired(): Boolean {
                val now: DateTime = DateTime.now(DateTimeZone.UTC)
                return now.isAfter(ttl)
        }

        fun priceWithTaxes(): String =  String.format("%.2f", total + (total * iva))
}