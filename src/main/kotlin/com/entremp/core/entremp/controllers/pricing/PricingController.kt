package com.entremp.core.entremp.controllers.pricing

import com.entremp.core.entremp.api.pricing.CreatePricingDTO
import com.entremp.core.entremp.controllers.Authenticated
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.service.PricingService
import com.entremp.core.entremp.service.ProductService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView

@RestController
@RequestMapping("/api/pricings")
class PricingController(
        private val pricingService: PricingService,
        private val productService: ProductService
): Authenticated {
    @GetMapping
    fun all(): Iterable<Pricing> {
        return pricingService.getAll()
    }

    @PostMapping
    fun save(@ModelAttribute storable: CreatePricingDTO,
             @RequestParam attachments: Array<MultipartFile> = emptyArray(),
             redirectAttributes: RedirectAttributes
    ): RedirectView {
        val auth: User? = getAuthUser()

        if(auth != null) {
            val product: Product = productService.find(storable.productId)

            val pricing: Pricing = pricingService.save(
                buyer = auth,
                product = product,
                quantity = storable.quantity,
                specifications = storable.specifications,
                sample = storable.sample,
                deliveryTerm = storable.deliveryTerm
            )

            // Save product loaded images
            attachments.map { image: MultipartFile ->
                pricingService.addAttachement(
                    id = pricing.id!!,
                    file = image
                )
            }

            val id: String = pricing.id !!

            redirectAttributes.addFlashAttribute("success", flashSuccess(productId = id))

            return RedirectView("/buyer/pricings/$id")
        } else {
            throw RuntimeException("Operation not allowed")
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody pricing: Pricing): Pricing {
        assert(pricing.id == id)

        val auth: User? = getAuthUser()

        if(auth != null){
            return pricingService.update(
                    id = id,
                    quantity = pricing.quantity,
                    specifications = pricing.specifications,
                    sample = pricing.sample,
                    deliveryTerm = pricing.deliveryTerm
            )
        } else {
            throw RuntimeException("Operation not allowed")
        }
    }

    @DeleteMapping("/{id}")
    fun remove(@PathVariable id: String) {
        pricingService.remove(id)
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: String): Pricing {
        return pricingService.find(id)
    }


    @PostMapping("/{id}/attachment")
    fun uploadAttachment(@PathVariable id: String, @RequestParam file: MultipartFile): Pricing {
        pricingService.addAttachement(id, file)
        return pricingService.find(id)
    }

    @DeleteMapping("/{id}/attachment/{attachementId}")
    fun removeAttachment(@PathVariable id: String, @PathVariable attachementId: String){
        pricingService.removeAttachement(attachementId)
    }


    private fun flashSuccess(productId: String): String =
        """
            <div class="col s12">
                <div class="card teal">
                    <div class="card-content white-text">
                        <h7>Tu producto se creo correctamente!</h7>
                        <h7>ID $productId</h7>
                    </div>
                </div>
            </div>
        """.trimIndent()
}