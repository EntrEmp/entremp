package com.entremp.core.entremp.controllers.product

import com.entremp.core.entremp.api.common.ChipDTO
import com.entremp.core.entremp.api.product.*
import com.entremp.core.entremp.controllers.Authenticated
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.product.ProductImage
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.service.ProductService
import com.entremp.core.entremp.support.ObjectMapperFactory
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView


@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
): Authenticated {

    val mapper: ObjectMapper = ObjectMapperFactory.camelCaseMapper

    @GetMapping
    fun all(): Iterable<Product> {
        return productService.getAll()
    }

    @PostMapping
    fun save(@ModelAttribute storable: FullCreateProductDTO,
             @RequestParam images: Array<MultipartFile> = emptyArray(),
             redirectAttributes: RedirectAttributes
    ): RedirectView {
        val auth: User? = getAuthUser()

        val tags: List<ChipDTO> = mapper.readValue(storable.tags)
        val certifications: List<ChipDTO> = mapper.readValue(storable.certifications)

        if(auth != null){
            val product: Product = productService.save(
                user = auth,
                name = storable.name,
                min = storable.minimum,
                max = storable.maximum,
                batch = storable.batch,
                description = storable.description
            )

            // Add tags to product
            tags.forEach { tagChip: ChipDTO ->
                //TODO associate tag & product
            }

            // Add tags to product
            certifications.forEach { certification: ChipDTO ->
                //TODO associate certification & product
            }

            // Save product loaded images
            images.map { image: MultipartFile ->
                productService.addImage(
                    id = product.id!!,
                    file = image
                )
            }

            val id: String = product.id!!

            redirectAttributes.addFlashAttribute("success", flashSuccess(productId = id))

            return RedirectView("/web/seller/products/$id")
        } else {
            throw RuntimeException("Operation not allowed")
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String,
               @RequestBody edit: ProductDTO): Product {
        assert(edit.id == id)

        val auth: User? = getAuthUser()

        if(auth != null){
            return productService.update(
                    id = id,
                    user = auth,
                    name = edit.name,
                    min = edit.min,
                    max = edit.max,
                    batch = edit.batch,
                    description = edit.description
            )
        } else {
            throw RuntimeException("Operation not allowed")
        }
    }

    @DeleteMapping("/{id}")
    fun remove(@PathVariable id: String) {
        productService.remove(id)
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: String): Product {
        return productService.find(id)
    }

    @PostMapping("/{id}/data")
    fun updateData(@PathVariable id: String,
                   @ModelAttribute storable: EditProductDataDTO,
                   redirectAttributes: RedirectAttributes
    ): RedirectView {
        val auth: User? = getAuthUser()

        val product: Product = productService.find(id)

        val attributes: List<ChipDTO> = mapper.readValue(storable.attributes)

        return if(auth != null && auth.id == product.user?.id){
            val edited = productService.update(
                id = id,
                user = auth,
                name = storable.name,
                min = product.minimum,
                max = product.maximum,
                batch = product.batchSize,
                description = storable.description
            )

            // Add tags to product
            attributes.forEach { attributeChip: ChipDTO ->
                //TODO associate tag & product
            }

            redirectAttributes.addFlashAttribute("success", flashSuccess(productId = id))

            RedirectView("/web/seller/products/$id/edit")
        } else {
            redirectAttributes.addFlashAttribute("error", flashSuccess(productId = id))

            RedirectView("/web/seller/products/$id/edit")
        }
    }

    @PostMapping("/{id}/stock")
    fun updateStock(@PathVariable id: String,
                   @ModelAttribute storable: EditProductStockDTO,
                   redirectAttributes: RedirectAttributes
    ): RedirectView {
        val auth: User? = getAuthUser()

        val product: Product = productService.find(id)

        return if(auth != null && auth.id == product.user?.id){
            val edited = productService.update(
                id = id,
                user = auth,
                name = product.name,
                min = storable.minimum,
                max = storable.maximum,
                batch = storable.batch,
                description = product.description
            )

            redirectAttributes.addFlashAttribute("success", flashSuccess(productId = id))

            RedirectView("/web/seller/products/$id/edit")
        } else {
            redirectAttributes.addFlashAttribute("error", flashSuccess(productId = id))

            RedirectView("/web/seller/products/$id/edit")
        }
    }


    @PostMapping("/{id}/tags-and-images")
    fun updateTags(@PathVariable id: String,
                   @ModelAttribute storable: EditProductTagsDTO,
                   @RequestParam images: Array<MultipartFile> = emptyArray(),
                   redirectAttributes: RedirectAttributes
    ): RedirectView {
        val auth: User? = getAuthUser()

        val product: Product = productService.find(id)

        val tags: List<ChipDTO> = mapper.readValue(storable.tags)

        return if(auth != null && auth.id == product.user?.id){

            // Add tags to product
            tags.forEach { tagsChip: ChipDTO ->
                //TODO associate tag & product
            }

            // Remove previous images
            product.images.map { image: ProductImage ->
                productService.removeImage(image.id!!)
            }

            // SSave new images
            images.map { image: MultipartFile ->
                productService.addImage(
                    id = product.id!!,
                    file = image
                )
            }

            redirectAttributes.addFlashAttribute("success", flashSuccess(productId = id))

            RedirectView("/web/seller/products/$id/edit")
        } else {
            redirectAttributes.addFlashAttribute("error", flashSuccess(productId = id))

            RedirectView("/web/seller/products/$id/edit")
        }
    }

    @PostMapping("/{id}/certifications")
    fun updateCertifications(@PathVariable id: String,
                             @ModelAttribute storable: EditProductCertificationsDTO,
                             redirectAttributes: RedirectAttributes
    ): RedirectView {
        val auth: User? = getAuthUser()

        val product: Product = productService.find(id)

        val certifications: List<ChipDTO> = mapper.readValue(storable.certifications)

        return if(auth != null && auth.id == product.user?.id){

            // Add tags to product
            certifications.forEach { certificationsChip: ChipDTO ->
                //TODO associate tag & product
            }

            redirectAttributes.addFlashAttribute("success", flashSuccess(productId = id))

            RedirectView("/web/seller/products/$id/edit")
        } else {
            redirectAttributes.addFlashAttribute("error", flashSuccess(productId = id))

            RedirectView("/web/seller/products/$id/edit")
        }
    }

    @PostMapping("/{id}/categories")
    fun addCategories(@PathVariable id: String, @RequestBody requested: ProductCategoryDTO): Product {
        requested.categories.forEach { categoryId ->
            productService.addCategory(id, categoryId)
        }

        return productService.find(id)
    }

    @DeleteMapping("/{id}/categories/{productCategoryId}")
    fun removeCategory(@PathVariable id: String, @PathVariable productCategoryId: String){
        productService.removeCategory(productCategoryId)
    }

    @PostMapping("/{id}/image")
    fun uploadImage(@PathVariable id: String, @RequestParam file: MultipartFile): Product {
        productService.addImage(id, file)
        return productService.find(id)

    }

    @DeleteMapping("/{id}/image/{imageId}")
    fun removeImage(@PathVariable id: String, @PathVariable imageId: String){
        productService.removeImage(imageId)
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