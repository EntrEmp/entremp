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

        val status: PricingStatus = PricingStatus.PENDING,

        @ElementCollection(targetClass=RequestedBilling::class)
        @Enumerated(EnumType.STRING)
        @CollectionTable(name="pricing_requested_billing")
        @Column(name="requested_billing")
        val requestedBilling: List<RequestedBilling> = emptyList()
){

    fun mainAttachment(): String {
        return pricingAttachements.firstOrNull()?.s3Link() ?: ""
    }

    fun providerName():String = provider!!.name

    fun pricingPending(): Boolean =
        status == PricingStatus.PENDING &&
                budget == null

    fun budgetPending(): Boolean =
        status == PricingStatus.PENDING &&
                budget != null

    fun pricingRejected(): Boolean =
        (status == PricingStatus.REJECTED ||
         status == PricingStatus.SAMPLE_REJECTED ) &&
                budget == null

    fun budgetRejected(): Boolean =
        (status == PricingStatus.REJECTED ||
         status == PricingStatus.SAMPLE_REJECTED ) &&
                budget != null

    fun approvedWithSample(): Boolean = approved() && sample

    fun approvedWithoutSample(): Boolean = approved() && !sample

    fun approved(): Boolean = status == PricingStatus.APPROVED

    fun pricingState(): String = when(status){
        PricingStatus.SAMPLE_REJECTED ->
            "<h6 style=\"color:red\"> Muestra rechazada</h6>\n"
        PricingStatus.REJECTED ->
            "<h6 style=\"color:red\"> Cotizacion rechazada</h6>\n"
        PricingStatus.APPROVED ->
            "<h6 style=\"color:darkgray\"> Cotizacion aprobada</h6>\n"
        else ->
            if(budget == null){
                "<h6 style=\"color:mediumseagreen\"> Cotizacion activa </h6>"
            } else {
                "<h6 style=\"color:lightseagreen\"> Cotizacion respondida </h6>"
            }
    }
}