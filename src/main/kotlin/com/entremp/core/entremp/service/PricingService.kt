package com.entremp.core.entremp.service


import com.entremp.core.entremp.data.pricing.PricingAttachementRepository
import com.entremp.core.entremp.data.pricing.PricingRepository
import com.entremp.core.entremp.data.sample.SampleRepository
import com.entremp.core.entremp.model.DeliveryTerm
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.pricing.PricingAttachement
import com.entremp.core.entremp.model.pricing.PricingStatus
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.sample.Sample
import com.entremp.core.entremp.model.user.User
import org.springframework.web.multipart.MultipartFile

import com.entremp.core.entremp.support.storage.S3FileStorageService

import com.entremp.core.entremp.support.JavaSupport.unwrap
import com.entremp.core.entremp.support.JavaSupport.extension
import java.io.File


class PricingService(
        private val pricingRepository: PricingRepository,
        private val sampleRepository: SampleRepository,
        private val pricingAttachementRepository: PricingAttachementRepository,
        private val fileStorageService: S3FileStorageService
) {

    fun getAll(): Iterable<Pricing> {
        return pricingRepository.findAll()
    }

    fun getActiveByProvider(provider: User): Iterable<Pricing> {
        return pricingRepository.findByProviderAndStatus(
            provider = provider,
            status = PricingStatus.PENDING
        )
    }

    fun getHistoryByProvider(provider: User): Iterable<Pricing> {
        val active: List<String> = pricingRepository.
            findByProviderAndStatus(
                provider = provider,
                status = PricingStatus.PENDING
            )
            .toList()
            .mapNotNull {pricing ->
                pricing.id
            }

        return pricingRepository
            .findByProvider(provider)
            .filterNot { pricing ->
                active.contains(pricing.id!!)
            }
    }

    fun getActiveByBuyer(buyer: User): Iterable<Pricing> {
        return pricingRepository.findByBuyerAndStatus(
            buyer = buyer,
            status = PricingStatus.PENDING
        )
    }

    fun getHistoryByBuyer(buyer: User): Iterable<Pricing> {
        val active: List<String> = pricingRepository.
            findByBuyerAndStatus(
                buyer = buyer,
                status = PricingStatus.PENDING
            )
            .toList()
            .mapNotNull {pricing ->
                pricing.id
            }

        return pricingRepository
            .findByBuyer(buyer)
            .filterNot { pricing ->
                active.contains(pricing.id!!)
            }
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

            val extension : String = file.extension() ?: "jpg"
            val attachementFile: File = fileStorageService.store(
                file = file,
                filename = "${attachement.id}",
                defaultExtension = extension
            )
            pricingAttachementRepository.save(
                attachement.copy(
                    fileLocation = attachementFile
                        .toURI()
                        .toURL()
                        .toString()
                )
            )

        } else {
            throw RuntimeException("Pricing not found for id $id")
        }
    }

    fun removeAttachement(attachementId: String){
        val attachement: PricingAttachement? = pricingAttachementRepository
            .findById(attachementId)
            .unwrap()

        if(attachement != null) {
            pricingAttachementRepository.deleteById(attachementId)
            fileStorageService.remove(
                filename = attachement.filename()
            )
        }
    }

    private fun sampleRequested(old: Boolean, new: Boolean): Boolean {
        return !old && new
    }

    private fun sampleUnrequested(old: Boolean, new: Boolean): Boolean {
        return old && !new
    }
}