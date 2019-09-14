package com.entremp.core.entremp.controllers.product

import com.entremp.core.entremp.api.product.ProductCategoryDTO
import com.entremp.core.entremp.api.product.ProductDTO
import com.entremp.core.entremp.controllers.Authenticated
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.service.ProductService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/products")
class ProductController(
        private val productService: ProductService
): Authenticated {

    @GetMapping
    fun all(): Iterable<Product> {
        return productService.getAll()
    }

    @PostMapping
    fun save(@RequestBody input: ProductDTO): Product {
        val auth: User? = getAuthUser()

        if(auth != null){
            return productService.save(
                    user = auth,
                    name = input.name,
                    min = input.min,
                    max = input.max,
                    batch = input.batch,
                    description = input.description
            )
        } else {
            throw RuntimeException("Operation not allowed")
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody edit: ProductDTO): Product {
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
}