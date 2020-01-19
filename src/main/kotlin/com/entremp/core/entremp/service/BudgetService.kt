package com.entremp.core.entremp.service



import com.entremp.core.entremp.data.budget.BudgetAttachementRepository
import com.entremp.core.entremp.data.budget.BudgetRepository
import com.entremp.core.entremp.data.pricing.PricingRepository
import com.entremp.core.entremp.model.DeliveryTerm
import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.model.budget.BudgetAttachement
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.support.storage.FileStorageService
import com.entremp.core.entremp.support.JavaSupport.unwrap
import org.springframework.web.multipart.MultipartFile
import java.net.URL
import com.entremp.core.entremp.support.JavaSupport.extension


class BudgetService(
        private val pricingRepository: PricingRepository,
        private val budgetRepository: BudgetRepository,
        private val budgetAttachementRepository: BudgetAttachementRepository,
        private val fileStorageService: FileStorageService
) {

    fun getApplicantBudgets(buyer: User): Iterable<Budget> {
        return pricingRepository.findByBuyer(buyer)
                                .filter { pricing ->
                                    pricing.budget != null
                                }.map { pricing ->
                                    pricing.budget!!
                                }
    }

    fun getRequestedBudgets(provider: User): Iterable<Budget> {
        return pricingRepository.findByProvider(provider)
                                .filter { pricing ->
                                    pricing.budget != null
                                }.map { pricing ->
                                    pricing.budget!!
                                }
    }

    fun save(
            pricing: Pricing,
            price: Double,
            iva: Double,
            deliveryConditions: String,
            paymentConditions: String,
            specifications: String,
            deliveryTerm: DeliveryTerm
    ): Budget {

        return budgetRepository.save(
                Budget(
                        pricing = pricing,
                        price = price,
                        iva = iva,
                        deliveryConditions = deliveryConditions,
                        paymentConditions = paymentConditions,
                        specifications = specifications,
                        deliveryTerm = deliveryTerm
                )
        )
    }

    fun update(
            id: String,
            price: Double,
            iva: Double,
            deliveryConditions: String,
            paymentConditions: String,
            specifications: String,
            deliveryTerm: DeliveryTerm
    ): Budget {
        val budget: Budget? = budgetRepository.findById(id).unwrap()

        if(budget != null){
            return budgetRepository.save(
                    budget.copy(
                            price = price,
                            iva = iva,
                            deliveryConditions = deliveryConditions,
                            paymentConditions = paymentConditions,
                            specifications = specifications,
                            deliveryTerm = deliveryTerm
                    )
            )
        } else {
            throw RuntimeException("Budget not found for id $id")
        }
    }


    fun remove(id: String) {
        val budget: Budget? = budgetRepository.findById(id).unwrap()
        if(budget != null){
            budgetRepository.deleteById(id)
        }
    }


    fun find(id: String): Budget {
        val budget: Budget? = budgetRepository.findById(id).unwrap()
        if(budget != null) {
            return budget
        } else {
            throw RuntimeException("Pricing not found for id $id")
        }
    }

    fun addAttachement(
            id: String,
            file: MultipartFile
    ) {
        val budget: Budget? = budgetRepository.findById(id).unwrap()

        if(budget != null) {
            val attachement: BudgetAttachement = budgetAttachementRepository.save(
                    BudgetAttachement(
                            budget = budget,
                            fileLocation = ""
                    )
            )

            val extension : String? = file.extension()
            val fileName = "${attachement.id}.$extension"
            val url: URL = fileStorageService.store(file, fileName)

            budgetAttachementRepository.save(
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
        budgetAttachementRepository.deleteById(productAttachementId)
    }


}