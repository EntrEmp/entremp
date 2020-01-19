package com.entremp.core.entremp.service



import com.entremp.core.entremp.data.pricing.PricingAttachementRepository
import com.entremp.core.entremp.data.pricing.PricingRepository
import com.entremp.core.entremp.data.sample.SampleRepository
import com.entremp.core.entremp.model.DeliveryTerm
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.pricing.PricingAttachement
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.sample.Sample
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.support.storage.FileStorageService
import com.entremp.core.entremp.support.JavaSupport.unwrap
import org.springframework.web.multipart.MultipartFile
import java.net.URL
import com.entremp.core.entremp.support.JavaSupport.extension


class PricingService(
        private val pricingRepository: PricingRepository,
        private val sampleRepository: SampleRepository,
        private val pricingAttachementRepository: PricingAttachementRepository,
        private val fileStorageService: FileStorageService
) {

    fun getAll(): Iterable<Pricing> {
        return pricingRepository.findAll()
    }

    fun save(
            buyer: User,
            product: Product,
            quantity: Long,
            specifications: String,
            sample: Boolean,
            deliveryTerm: DeliveryTerm
    ): Pricing {
        val pricing: Pricing = pricingRepository.save(
                Pricing(
                        buyer = buyer,
                        provider = product.user,
                        product = product,
                        quantity = quantity,
                        specifications = specifications,
                        sample = sample,
                        deliveryTerm = deliveryTerm
                )
        )

        // Create a Sample only if requested
        if(sample){
            sampleRepository.save(
                    Sample(
                            pricing = pricing
                    )
            )
        }

        return pricing
    }

    fun update(
            id: String,
            quantity: Long,
            specifications: String,
            sample: Boolean,
            deliveryTerm: DeliveryTerm
    ): Pricing {
        val pricing: Pricing? = pricingRepository.findById(id).unwrap()

        if(pricing != null){
            // TODO validate pricing status

            if(sampleRequested(pricing.sample, sample)){
                sampleRepository.save(
                        Sample(
                                pricing = pricing
                        )
                )
            } else if(sampleUnrequested(pricing.sample, sample)){
                sampleRepository.deleteByPricing(pricing)
            }

            return pricingRepository.save(
                    pricing.copy(
                            quantity = quantity,
                            specifications = specifications,
                            sample = sample,
                            deliveryTerm = deliveryTerm
                    )
            )
        } else {
            throw RuntimeException("Pricing not found for id $id")
        }
    }

    fun remove(id: String) {
        val pricing: Pricing? = pricingRepository.findById(id).unwrap()
        if(pricing != null && pricing.budget == null){
            if(pricing.sample){
                sampleRepository.deleteByPricing(pricing)
            }
            pricingRepository.deleteById(id)
        }
    }


    fun find(id: String): Pricing {
        val pricing: Pricing? = pricingRepository.findById(id).unwrap()
        if(pricing != null) {
            return pricing
        } else {
            throw RuntimeException("Pricing not found for id $id")
        }
    }


    fun addAttachement(
            id: String,
            file: MultipartFile
    ) {
        val pricing: Pricing? = pricingRepository.findById(id).unwrap()

        if(pricing != null) {
            val attachement: PricingAttachement = pricingAttachementRepository.save(
                    PricingAttachement(
                            pricing = pricing,
                            fileLocation = ""
                    )
            )

            val extension : String? = file.extension()
            val fileName = "${attachement.id}.$extension"
            val url: URL = fileStorageService.store(file, fileName)

            pricingAttachementRepository.save(
                    attachement.copy(
                            fileLocation = url.toString()
                    )
            )

        } else {
            throw RuntimeException("Product not found for id $id")
        }
    }

    fun removettachement(productAttachementId: String){
        // TODO remove image from file storage server
        pricingAttachementRepository.deleteById(productAttachementId)
    }

    private fun sampleRequested(old: Boolean, new: Boolean): Boolean {
        return !old && new
    }

    private fun sampleUnrequested(old: Boolean, new: Boolean): Boolean {
        return old && !new
    }
}