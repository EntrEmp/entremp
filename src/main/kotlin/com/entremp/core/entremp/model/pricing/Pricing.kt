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
){

    fun mainAttachment(): String {
        return pricingAttachements.firstOrNull()?.s3Link() ?: ""
    }

    fun providerName():String = provider!!.name

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

    fun indexProviderActions(): String = when(status) {
        PricingStatus.PENDING ->
            if (budget == null) {
                """
                <div class="row">
                    <a class="waves-effect waves-light btn-small modal-trigger"
                       href="#budgetCreate_$id">Ofrecer Cotización</a>
                </div>
                <div class="row">
                    <a class="waves-effect waves-light btn-small modal-trigger"
                       href="#budgetCancel_$id">Rechazar Cotización</a>
                </div>
                <a class="status-detail" href="/seller/pricings/$id">Ver detalle</a>
                """.trimIndent()
            } else {
                """
                <div class="row">
                    <a class="waves-effect waves-light btn-small modal-trigger"
                       href="#budgetEdit_$id">Editar Cotización</a>
                </div>
                <a class="status-detail" href="/seller/pricings/$id">Ver detalle</a>    
                """.trimIndent()
            }

        PricingStatus.APPROVED ->
            """
                 <div class="row">
                    <a class="waves-effect waves-light btn">Ver Mensajes</a>
                </div>
                <a class="status-detail" href="/seller/pricings/$id">Ver detalle</a>    
            """.trimIndent()
        else ->
            """
                <p>
                </p>
            """.trimIndent()
    }

    fun providerActions(): String = when(status) {
        PricingStatus.PENDING ->
            if (budget == null) {
                """
                <p>
                    <a class="waves-effect waves-light btn modal-trigger" href="#budgetCreate_$id">Ofrecer Cotización</a>
                    <a class="waves-effect waves-light btn modal-trigger" href="#budgetCancel_$id">Rechazar Cotización</a>
                </p>
                """.trimIndent()
            } else {
                """
                <p>
                    <a class="waves-effect waves-light btn modal-trigger" href="#budgetEdit_$id">Editar Cotización</a>
                </p>
                """.trimIndent()
            }

        PricingStatus.APPROVED ->
            """
                <p>
                    <a class="waves-effect waves-light btn">Ver Mensajes</a>
                </p>
            """.trimIndent()
        else ->
            """
                <p>
                </p>
            """.trimIndent()
    }

    fun indexBuyerActions(): String = when(status){
        PricingStatus.PENDING ->
            if(budget != null){
                """
                <div class="row">
                    <a class="waves-effect waves-light btn-small modal-trigger"
                       href="#budgetAccept_$id">Aprobar Cotización</a>
                </div>
                <div class="row">
                    <a class="waves-effect waves-light btn-small"
                       href="#$id">Descargar Cotización</a>
                </div>
                <a class="status-detail" href="/buyer/pricings/$id">Ver detalle</a>
                """.trimIndent()
            } else {
                "<p> Aguardando una respuesta del proveedor</p>"
            }

        PricingStatus.APPROVED ->
            if(sample){
                """
                <div class="row">
                    <a class="waves-effect waves-light btn-small modal-trigger"
                       href="#rejectSample_$id">Rechazar Muestra</a>
                </div>
                <div class="row">
                    <a class="waves-effect waves-light btn-small"
                       href="#">Enviar Mensaje</a>
                </div>
                <a class="status-detail" href="/buyer/pricings/$id">Ver detalle</a>
                """.trimIndent()
            } else{
                """
                <div class="row">
                    <a class="waves-effect waves-light btn-small"
                       href="#">Enviar Mensaje</a>
                </div>
                """.trimIndent()
            }

        else ->
            """
                <div class="row">
                    <a class="waves-effect waves-light btn-small modal-trigger"
                       href="#">Cotizar nuevamente</a>
                </div>
                <div class="row">
                    <a class="waves-effect waves-light btn-small"
                       href="#">Ver Mensajes</a>
                </div>
            """.trimIndent()
    }

    fun buyerActions(): String = when(status){
        PricingStatus.PENDING ->
            if(budget != null){
                """
                    <p>
                        <a class="waves-effect waves-light btn modal-trigger" 
                           href="#budgetAccept_$id">Aprobar Cotización</a>
                        <a class="waves-effect waves-light btn">Descargar Cotización</a>
                    </p>
                """.trimIndent()
            } else {
                "<p> Aguardando una respuesta del proveedor</p>"
            }

        PricingStatus.APPROVED ->
            if(sample){
                """
                    <p>
                        <a class="waves-effect waves-light btn modal-trigger"
                           href="#rejectSample_$id">Rechazar Muestra</a>
                        <a class="waves-effect waves-light btn">Enviar Mensajes</a>
                    </p>
                """.trimIndent()
            } else{
                """
                    <p>
                        <a class="waves-effect waves-light btn">Enviar Mensajes</a>
                    </p>
                """.trimIndent()
            }

        else ->
            """
                <p>
                    <a class="waves-effect waves-light btn">Cotizar nuevamente</a>
                    <a class="waves-effect waves-light btn">Ver Mensajes</a>
                </p>
            """.trimIndent()
    }
}