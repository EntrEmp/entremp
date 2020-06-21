package com.entremp.core.entremp.service

import com.entremp.core.entremp.data.budget.BudgetAttachementRepository
import com.entremp.core.entremp.data.budget.BudgetRepository
import com.entremp.core.entremp.data.pricing.PricingRepository
import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.model.budget.BudgetAttachement
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.user.User
import org.springframework.web.multipart.MultipartFile
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

import com.entremp.core.entremp.support.JavaSupport.unwrap
import com.entremp.core.entremp.support.JavaSupport.extension
import com.entremp.core.entremp.support.storage.S3FileStorageService

import java.io.File

class BudgetService(
        private val pricingRepository: PricingRepository,
        private val budgetRepository: BudgetRepository,
        private val budgetAttachementRepository: BudgetAttachementRepository,
        private val fileStorageService: S3FileStorageService
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
        quantity: Long,
        iva: Double,
        total: Double,
        billing: String,
        selectedBilling: String,
        ttl: DateTime,
        deliveryAfterConfirm: Long
    ): Budget {

        return budgetRepository.save(
            Budget(
                pricing = pricing,

                price = price,
                quantity = quantity,

                billing = billing,
                selectedBilling = selectedBilling,
                iva = iva,
                total = total,

                ttl = ttl,
                deliveryAfterConfirm = deliveryAfterConfirm
            )
        )
    }

    fun confirm(id: String): Budget {
        val budget: Budget? = budgetRepository.findById(id).unwrap()

        if(budget != null && budget.confirmationDate ==  null){
            val now: DateTime = DateTime.now(DateTimeZone.UTC)
            return budgetRepository.save(
                budget.copy(
                    confirmationDate = now,
                    deliveryDate = now.plusDays(budget.deliveryAfterConfirm.toInt())
                )
            )
        } else {
            throw RuntimeException("Budget not found for id $id")
        }
    }

    fun update(
            id: String,
            price: Double,
            quantity: Long,
            iva: Double,
            total: Double,
            billing: String,
            selectedBilling: String,
            ttl: DateTime,
            deliveryAfterConfirm: Long
    ): Budget {
        val budget: Budget? = budgetRepository.findById(id).unwrap()

        if(budget != null){
            return budgetRepository.save(
                    budget.copy(
                        price = price,
                        quantity = quantity,

                        billing = billing,
                        selectedBilling = billing,
                        iva = iva,
                        total = total,

                        ttl = ttl,
                        deliveryAfterConfirm = deliveryAfterConfirm
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
            throw RuntimeException("Budget not found for id $id")
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

            val extension : String = file.extension() ?: "jpg"
            val attachementFile: File = fileStorageService.store(
                file = file,
                filename = "${attachement.id}",
                defaultExtension = extension
            )
            budgetAttachementRepository.save(
                attachement.copy(
                    fileLocation = attachementFile
                        .toURI()
                        .toURL()
                        .toString()
                )
            )

        } else {
            throw RuntimeException("Budget not found for id $id")
        }
    }

    fun removettachement(attachementId: String){
        val attachement: BudgetAttachement? = budgetAttachementRepository
            .findById(attachementId)
            .unwrap()

        if(attachement != null) {
            budgetAttachementRepository.deleteById(attachementId)
            fileStorageService.remove(
                filename = attachement.filename()
            )
        }
    }

}