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
import org.apache.commons.codec.binary.Base64
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
): Authenticated {

    val mapper: ObjectMapper = ObjectMapperFactory.camelCaseMapper

    @GetMapping
    fun list(): Iterable<Product> {
        return productService.getAll()
    }

    @PostMapping("/filter")
    fun filter(@ModelAttribute filter: ProductFilterDTO,
               httpServletResponse: HttpServletResponse,
               redirectAttributes: RedirectAttributes): RedirectView {

        val filterCookie = Cookie(
            "searchFilter",
            Base64.encodeBase64String(
                mapper.writeValueAsString(filter).toByteArray()
            )
        )
        filterCookie.maxAge = 60*10
        filterCookie.path = "/web/buyer/products"

        httpServletResponse.addCookie(filterCookie)

        return RedirectView("/web/buyer/products")
    }

    @PostMapping("/text-search")
    fun search(@RequestParam searchCriteria: String,
               httpServletResponse: HttpServletResponse,
               redirectAttributes: RedirectAttributes): RedirectView {

        val filter = ProductFilterDTO(
            criteria = searchCriteria
        )

        val filterCookie = Cookie(
            "searchFilter",
            Base64.encodeBase64String(
                mapper.writeValueAsString(filter).toByteArray()
            )
        )
        filterCookie.maxAge = 60*10
        filterCookie.path = "/web/buyer/products"

        httpServletResponse.addCookie(filterCookie)

        return RedirectView("/web/buyer/products")
    }

    @PostMapping
    fun save(@ModelAttribute storable: FullCreateProductDTO,
             @RequestParam images: Array<MultipartFile> = emptyArray(),
             redirectAttributes: RedirectAttributes
    ): RedirectView {
        val auth: User? = getAuthUser()

        val tags: List<ChipDTO> = mapper.readValue(storable.tags)
        val certifications: List<ChipDTO> = mapper.readValue(storable.certifications)
        val categories: List<ChipDTO> = mapper.readValue(storable.categories)
        val attributes: List<ChipDTO> = mapper.readValue(storable.attributes)

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
            productService.addTags(
                product = product,
                tags = tags.map { chip ->
                    chip.tag
                }
            )

            // Add tags to product
            productService.addCertifications(
                product = product,
                certifications = certifications.map { chip ->
                    chip.tag
                }
            )

            // Add attributes to product
            productService.addCategories(
                product = product,
                categories = categories
                    .map { chip ->
                        chip.tag
                    }
            )

            // Add attributes to product
//            productService.inactivateAttributes(
//                product = product,
//                inactive = emptyList(),
//                active = attributes.map { chip ->
//                    chip.tag
//                }
//            )

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

        val categories: List<ChipDTO> = mapper.readValue(storable.categories)
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

            val inactive: List<String> = edited
                .productAttributes
                .filter { attribute ->
                    attribute.active
                }
                .mapNotNull { attribute ->
                    attribute.attribute?.name
                }
                .minus(
                    attributes
                        .map { chip ->
                            chip.tag
                        }
                        .toTypedArray()
                )

            // Add categories and attributes
            productService.addCategories(
                product = edited,
                categories = categories
                    .map { chip ->
                        chip.tag
                    }
            )

            productService.inactivateAttributes(
                product = edited,
                inactive = inactive,
                active = attributes
                    .map { chip ->
                        chip.tag
                    }
            )

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
            productService.update(
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
            productService.addTags(
                product = product,
                tags = tags.map { chip ->
                    chip.tag
                }
            )

            // Remove previous images
            product
                .images
                .map { image: ProductImage ->
                    productService.removeImage(image.id!!)
            }

            // SSave new images
            images
                .map { image: MultipartFile ->
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
            productService.addCertifications(
                product = product,
                certifications = certifications.map { chip ->
                    chip.tag
                }
            )

            redirectAttributes.addFlashAttribute("success", flashSuccess(productId = id))

            RedirectView("/web/seller/products/$id/edit")
        } else {
            redirectAttributes.addFlashAttribute("error", flashSuccess(productId = id))

            RedirectView("/web/seller/products/$id/edit")
        }
    }

    @GetMapping("/{id}/favorite")
    fun favorite(@PathVariable id: String): ResponseEntity<String> {
        val auth: User? = getAuthUser()

        productService.favorite(
            id = id,
            user = auth !!
        )

        return ResponseEntity.ok("OK")
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