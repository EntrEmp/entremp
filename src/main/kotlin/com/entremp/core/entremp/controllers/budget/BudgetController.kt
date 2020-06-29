package com.entremp.core.entremp.controllers.budget

import com.entremp.core.entremp.api.budget.CreateBudgetDTO
import com.entremp.core.entremp.api.budget.EditBudgetDTO
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
import com.entremp.core.entremp.model.pricing.PricingStatus
import com.entremp.core.entremp.model.review.Review
import com.entremp.core.entremp.model.review.ReviewQualification
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.service.BudgetService
import com.entremp.core.entremp.service.PricingService
import com.entremp.core.entremp.support.storage.FileStorageService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.net.URL
import java.util.*

import com.entremp.core.entremp.support.JavaSupport.extension
import com.entremp.core.entremp.support.JavaSupport.unwrap
import java.io.File

@RestController
@RequestMapping("/api/budgets")
class BudgetController(
    private val pricingService: PricingService,
    private val budgetService: BudgetService,

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

    @PostMapping("/{id}/accept")
    fun accept(@PathVariable id: String,
               redirectAttributes: RedirectAttributes): RedirectView {
        val auth: User? = getAuthUser()

        if(auth != null) {
            val budget: Budget = budgetService.find(id)

            budgetService.confirm(id)

            val pricing: Pricing = budget.pricing!!
            pricingRepository.save(
                pricing.copy(
                    status = PricingStatus.APPROVED
                )
            )

            val chat: Chat = chatRepository.save(
                Chat(
                    buyer = pricing.buyer,
                    provider = pricing.provider
                )
            )

            return RedirectView("/buyer/messages/${chat.id!!}")
        } else {
            throw RuntimeException("Operation not allowed")
        }
    }

    @PostMapping("/reject")
    fun reject(@RequestParam pricingId: String,
               redirectAttributes: RedirectAttributes): RedirectView {
        val auth: User? = getAuthUser()

        if(auth != null) {
            val pricing: Pricing = pricingService.find(pricingId)

            pricingRepository.save(
                pricing.copy(
                    status = PricingStatus.REJECTED
                )
            )

            return RedirectView("/seller/pricings/$pricingId")
        } else {
            throw RuntimeException("Operation not allowed")
        }
    }

    @PostMapping
    fun save(@ModelAttribute storable: CreateBudgetDTO,
             @RequestParam attachments: Array<MultipartFile> = emptyArray(),
             redirectAttributes: RedirectAttributes
    ): RedirectView {
        val auth: User? = getAuthUser()

        if(auth != null) {

            val pricing: Pricing = pricingService.find(storable.pricingId)

            val budget = budgetService.save(
                pricing = pricing,

                price = storable.price,
                quantity = storable.quantity,

                billing = storable.billing(),
                selectedBilling = storable.selectedBilling,
                iva = storable.iva,
                total = storable.total,

                ttl = storable.ttlDateTime(),
                deliveryAfterConfirm = storable.delivery
            )

            // Save product loaded images
            attachments
                .filterNot{ image ->
                    image.isEmpty
                }
                .map { image: MultipartFile ->
                budgetService.addAttachement(
                    id = budget.id!!,
                    file = image
                )
            }

            val id: String = pricing.id !!

            redirectAttributes.addFlashAttribute("success", flashSuccess(productId = id))

            return RedirectView("/seller/pricings/$id")
        } else {
            throw RuntimeException("Operation not allowed")
        }
    }

    @PostMapping("/{id}/update")
    fun update(@PathVariable id: String,
               @ModelAttribute storable: EditBudgetDTO,
               @RequestParam attachments: Array<MultipartFile> = emptyArray(),
               redirectAttributes: RedirectAttributes
    ): RedirectView {
        val auth: User? = getAuthUser()

        if(auth != null) {

            val budget: Budget = budgetService.update(
                id = id,

                price = storable.price,
                quantity = storable.quantity,

                billing = storable.billing(),
                selectedBilling = storable.selectedBilling,
                iva = storable.iva,
                total = storable.total,

                ttl = storable.ttlDateTime(),
                deliveryAfterConfirm = storable.delivery
            )

            // Save product loaded images
            attachments.map { image: MultipartFile ->
                budgetService.addAttachement(
                    id = budget.id!!,
                    file = image
                )
            }

            val id: String = budget.pricing!!.id !!

            redirectAttributes.addFlashAttribute("success", flashSuccess(productId = id))

            return RedirectView("/seller/pricings/$id")
        } else {
            throw RuntimeException("Operation not allowed")
        }
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
            val extension : String = file.extension() ?: "pdf"
            val storedFile: File = fileStorageService.store(
                file = file,
                filename = "${image.id}",
                defaultExtension = extension
            )

            val budgetImage = BudgetAttachement(
                id = image.id,
                budget = budget,
                fileLocation = storedFile
                    .toURI()
                    .toURL()
                    .toString()
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