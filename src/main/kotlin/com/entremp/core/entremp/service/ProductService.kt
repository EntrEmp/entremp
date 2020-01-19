package com.entremp.core.entremp.service

import com.entremp.core.entremp.data.CategoriesRepository
import com.entremp.core.entremp.data.product.ProductAttributeRepository
import com.entremp.core.entremp.data.product.ProductCategoryRepository
import com.entremp.core.entremp.data.product.ProductImageRepository
import com.entremp.core.entremp.data.product.ProductsRepository
import com.entremp.core.entremp.model.Category
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.product.ProductAttribute
import com.entremp.core.entremp.model.product.ProductCategory
import com.entremp.core.entremp.model.product.ProductImage
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.support.JavaSupport.extension

import com.entremp.core.entremp.support.JavaSupport.unwrap
import com.entremp.core.entremp.support.storage.S3FileStorageService
import org.springframework.web.multipart.MultipartFile
import java.net.URL

class ProductService(
        private val productsRepository: ProductsRepository,
        private val categoriesRepository: CategoriesRepository,
        private val productCategoriesRepository: ProductCategoryRepository,
        private val productAttributeRepository: ProductAttributeRepository,
        private val productImageRepository: ProductImageRepository,
        private val fileStorageService: S3FileStorageService

) {

    fun getAll(): Iterable<Product> {
        return productsRepository.findAll()
    }

    fun save(
            user: User,
            name: String,
            min: Int,
            max: Int,
            batch: Int,
            description: String?
    ): Product {
        return productsRepository.save(
                Product(
                        user = user,
                        name = name,
                        minimum = min,
                        maximum = max,
                        batchSize = batch,
                        description = description ?: ""
                )
        )
    }

    fun update(
            id: String,
            user: User,
            name: String,
            min: Int,
            max: Int,
            batch: Int,
            description: String?
    ): Product {
        val product: Product? = productsRepository.findById(id).unwrap()

        if(product != null){
            return productsRepository.save(
                    product.copy(
                            user = user,
                            name = name,
                            minimum = min,
                            maximum = max,
                            batchSize = batch,
                            description = description ?: ""
                    )
            )
        } else {
            throw RuntimeException("Product not found for id $id")
        }
    }


    fun remove(id: String) {
        return productsRepository.deleteById(id)
    }

    fun find(id: String): Product {
        val product: Product? = productsRepository.findById(id).unwrap()
        if(product != null) {
            return product
        } else {
            throw RuntimeException("Product not found for id $id")
        }
    }

    fun addCategory(
            productId: String,
            categoryId: String
    ) {
        val category: Category? = categoriesRepository.findById(categoryId).unwrap()
        val product: Product? = productsRepository.findById(productId).unwrap()

        if(category != null && product != null){
            productCategoriesRepository.save(
                    ProductCategory(
                        product = product,
                        category = category
                    )
            )

            category.attributes
                    .forEach { attribute ->
                        productAttributeRepository.save(
                            ProductAttribute(
                                product = product,
                                attribute = attribute
                            )
                    )
            }

        } else {
            throw RuntimeException("Either product (for id $productId) or category (for id $categoryId) do not exist")
        }
    }

    fun removeCategory(productCategoryId: String){
        val productCategory: ProductCategory? = productCategoriesRepository.findById(productCategoryId).unwrap()

        if (productCategory != null) {
            productCategory.category
                    ?.attributes
                    ?.forEach { attribute ->
                        productAttributeRepository.deleteByProductAndAttribute(
                            product = productCategory.product,
                            attribute = attribute
                        )
                    }
            productCategoriesRepository.deleteById(productCategoryId)
        }

    }

    fun addImage(
            id: String,
            file: MultipartFile
    ) {
        val product: Product? = productsRepository.findById(id).unwrap()

        if(product != null) {
            val image: ProductImage = productImageRepository.save(
                    ProductImage(
                            fileLocation = ""
                    ).copy(product = product)
            )

            val extension : String? = file.extension()
            val fileName = "${image.id}.$extension"
            val url: URL = fileStorageService.store(file, fileName)

            productImageRepository.save(
                    image.copy(
                            fileLocation = url.toString()
                    )
            )

        } else {
            throw RuntimeException("Product not found for id $id")
        }
    }

    fun removeImage(productImageId: String){
        // TODO remove image from file storage server
        productImageRepository.deleteById(productImageId)
    }

}