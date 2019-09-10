package com.entremp.core.entremp.controllers.pricing

import com.entremp.core.entremp.api.pricing.PricingDTO
import com.entremp.core.entremp.controllers.Authenticated
import com.entremp.core.entremp.data.pricing.PricingAttachementRepository
import com.entremp.core.entremp.data.pricing.PricingRepository
import com.entremp.core.entremp.data.product.ProductsRepository
import com.entremp.core.entremp.data.sample.SampleRepository
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.pricing.PricingAttachement
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.sample.Sample
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.service.PricingService
import com.entremp.core.entremp.service.ProductService
import com.entremp.core.entremp.support.FileStorageService
import com.entremp.core.entremp.support.JavaSupport.extension
import org.springframework.web.bind.annotation.*
import com.entremp.core.entremp.support.JavaSupport.unwrap
import org.springframework.web.multipart.MultipartFile
import java.net.URL
import java.util.*

@RestController
@RequestMapping("/pricings")
class PricingController(
        private val pricingService: PricingService,
        private val productService: ProductService
): Authenticated {
    @GetMapping
    fun all(): Iterable<Pricing> {
        return pricingService.getAll()
    }

    @PostMapping
    fun save(@RequestBody input: PricingDTO): Pricing {
        val auth: User? = getAuthUser()

        if(auth != null) {
            val product: Product = productService.find(input.productId)

            return pricingService.save(
                    buyer = auth,
                    product = product,
                    quantity = input.quantity,
                    specifications = input.specifications,
                    sample = input.sample,
                    deliveryTerm = input.deliveryTerm
            )
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
        pricingService.removettachement(attachementId)
    }

}