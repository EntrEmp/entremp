package com.entremp.core.entremp.controllers.budget

import com.entremp.core.entremp.api.budget.BudgetDTO
import com.entremp.core.entremp.api.review.ReviewDTO
import com.entremp.core.entremp.api.review.ReviewQualificationDTO
import com.entremp.core.entremp.controllers.Authenticated
import com.entremp.core.entremp.data.budget.BudgetAttachementRepository
import com.entremp.core.entremp.data.budget.BudgetRepository
import com.entremp.core.entremp.data.chat.ChatRepository
import com.entremp.core.entremp.data.pricing.PricingRepository
import com.entremp.core.entremp.data.review.ReviewQualificationRepository
import com.entremp.core.entremp.data.review.ReviewRepository
import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.model.budget.BudgetAttachement
import com.entremp.core.entremp.model.chat.Chat
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.review.Review
import com.entremp.core.entremp.model.review.ReviewQualification
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.support.storage.FileStorageService
import com.entremp.core.entremp.support.JavaSupport.extension
import com.entremp.core.entremp.support.JavaSupport.unwrap
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URL
import java.util.*

@RestController
@RequestMapping("/budgets")
class BudgetController(
        private val budgetRepository: BudgetRepository,
        private val budgetAttachementRepository: BudgetAttachementRepository,
        private val pricingRepository: PricingRepository,
        private val reviewRepository: ReviewRepository,
        private val qualificationRepository: ReviewQualificationRepository,
        private val chatRepository: ChatRepository,
        private val fileStorageService: FileStorageService
): Authenticated {

    @GetMapping
    fun all(): Iterable<Budget> {
        val auth: User? = getAuthUser()

        return pricingRepository
                .findByBuyerOrProvider(auth, auth)
                .map { pricing ->
                    pricing.budget
                }
                .filterNotNull()
    }

    @PostMapping
    fun save(@RequestBody input: BudgetDTO): Budget {
        val auth: User? = getAuthUser()

        val pricing: Pricing? = pricingRepository.findById(input.pricingId).unwrap()

        assert(pricing?.provider != auth)

        val item = Budget(
                pricing = pricing,
                price = input.price,
                iva = input.iva,
                deliveryConditions = input.deliveryConditions,
                paymentConditions = input.paymentConditions,
                specifications = input.specifications,
                deliveryTerm = input.deliveryTerm
        )

        return budgetRepository.save(item)
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: String): Optional<Budget> {
        return budgetRepository.findById(id)
    }

    @PostMapping("/{id}/select")
    fun select(@PathVariable id: String): Budget? {
        val auth: User? = getAuthUser()

        val budget: Budget? = budgetRepository.findById(id).unwrap()

        if(budget != null){
            val pricing: Pricing? = budget.pricing

            if(pricing?.buyer!!.id != auth!!.id){
                throw RuntimeException("Unauthorized: only a pricing\'s buyer is allowed to select a specific budget")
            }

            val review = Review(
                    buyer = pricing.buyer,
                    provider = pricing.provider,
                    budget = budget,
                    comment = ""
            )

            val chat = Chat(
                    buyer = pricing.buyer,
                    provider = pricing.provider
            )

            reviewRepository.save(review)
            chatRepository.save(chat)
        }

        return budgetRepository.findById(id).unwrap()
    }

    @PostMapping("/{id}/attachment")
    fun uploadAttachment(@PathVariable id: String, @RequestParam file: MultipartFile): Optional<Budget> {
        val budget: Budget? = budgetRepository.findById(id).unwrap()

        val image: BudgetAttachement = budgetAttachementRepository.save(
                BudgetAttachement(
                        budget = budget,
                        fileLocation = ""
                )
        )

        if (budget != null) {
            val extension : String? = file.extension()
            val fileName = "${image.id}.$extension"
            val url: URL = fileStorageService.store(file, fileName)

            val budgetImage = BudgetAttachement(
                    id = image.id,
                    budget = budget,
                    fileLocation = url.toString()
            )

            budgetAttachementRepository.save(budgetImage)
        }

        return budgetRepository.findById(id)
    }

    @DeleteMapping("/{id}/attachment/{imageId}")
    fun removeAttachment(@PathVariable id: String, @PathVariable imageId: String){
        budgetRepository.deleteById(imageId)
    }

    @PostMapping("/{id}/review")
    fun executeReview(@PathVariable id: String, @RequestBody input: ReviewDTO): Review {
        val budget: Budget? = budgetRepository.findById(id).unwrap()

        val review: Review = reviewRepository.findByBudget(budget)!!

        reviewRepository.save(
                Review(
                        id = review.id,
                        buyer = review.buyer,
                        provider = review.provider,
                        comment = input.comment!!
                )
        )

        input.qualifications.forEach { data: ReviewQualificationDTO ->
            qualificationRepository.save(

                    ReviewQualification(
                            review = review,
                            qualificationType = data.qualificationType,
                            qualification = data.qualification!!
                    )
            )
        }

        return reviewRepository.findById(review.id!!).get()
    }

}