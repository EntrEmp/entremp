package com.entremp.core.entremp.service

import com.entremp.core.entremp.api.product.ProductFilterDTO
import com.entremp.core.entremp.data.AttributesRepository
import com.entremp.core.entremp.data.CategoriesRepository
import com.entremp.core.entremp.data.CertificationTagsRepository
import com.entremp.core.entremp.data.TagsRepository
import com.entremp.core.entremp.data.product.*
import com.entremp.core.entremp.model.Category
import com.entremp.core.entremp.model.CertificationTag
import com.entremp.core.entremp.model.Tag
import com.entremp.core.entremp.model.product.*
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.support.JavaSupport.extension

import com.entremp.core.entremp.support.storage.S3FileStorageService
import org.springframework.web.multipart.MultipartFile

import com.entremp.core.entremp.support.JavaSupport.unwrap
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.io.File

class ProductService(
        private val productsRepository: ProductsRepository,

        private val favoriteProductRepository: FavoriteProductRepository,

        private val categoriesRepository: CategoriesRepository,
        private val attributeRepository: AttributesRepository,
        private val certificationTagsRepository: CertificationTagsRepository,
        private val tagsRepository: TagsRepository,

        private val productCategoriesRepository: ProductCategoryRepository,
        private val productAttributeRepository: ProductAttributeRepository,
        private val productCertificationRepository: ProductCertificationRepository,
        private val productTagRepository: ProductTagRepository,

        private val productImageRepository: ProductImageRepository,

        private val fileStorageService: S3FileStorageService
) {

    fun getAll(): Iterable<Product> =  productsRepository.findAll()

    fun getFavorites(user: User): List<FavoriteProduct> = favoriteProductRepository.findByUser(user)

    fun save(
            user: User,
            name: String,
            min: Int,
            max: Int,
            batch: Int,
            description: String?
    ): Product {

        val storable = Product(
            user = user,
            name = name,
            minimum = min,
            maximum = max,
            batchSize = batch,
            description = description ?: ""
        )

        return productsRepository.save(storable)
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
        val stored: Product? = productsRepository.findById(id).unwrap()

        if(stored != null){

            val storable = stored.copy(
                user = user,
                name = name,
                minimum = min,
                maximum = max,
                batchSize = batch,
                description = description ?: ""
            )

            return productsRepository.save(storable)
        } else {
            throw RuntimeException("Product not found for id $id")
        }
    }

    fun productsForTag(tagString: String): Page<Product> {
        val criteriaTags: List<Tag> =
            tagsRepository
                .findByNameContainingIgnoreCase(
                    name = tagString
                )

        val searched: List<ProductTag> =
            productTagRepository
                .findByTagIdIn(
                    criteriaTags
                        .mapNotNull { tag ->
                            tag.id
                        }
                )

        val tags: List<String> = searched
            .mapNotNull{ element ->
                element.product?.id
            }

        return productsRepository
            .findAll(
                ProductFilterSpecification(
                    ids = tags,
                    minStock = null,
                    maxStock = null,
                    minBatch = null,
                    maxBatch = null,
                    searchTerm = null
                ),
                PageRequest.of(
                    0,
                    4
                )
            )

    }

    fun filter(productFilter: ProductFilterDTO?): Page<Product> {
        val page: Int = productFilter
            ?.searchPage
            ?: 1

        return if(productFilter != null && productFilter.isNotEmpty()){

            val certifications: List<String> =
                if(productFilter.certifications.isNotEmpty()){
                    productCertificationRepository
                        .findAll(
                            ProductCertificationSpecification(
                                certifications = certificationTagsRepository
                                    .findAllById(
                                        productFilter.certifications
                                    ).toList()
                            )
                        )
                        .mapNotNull{ element ->
                            element.product?.id
                        }
                } else {
                    emptyList()
                }

            val attributes: List<String> =
                if(productFilter.attributes.isNotEmpty()){
                    productAttributeRepository
                        .findAll(
                            ProductAttributeSpecification(
                                attributes = attributeRepository
                                    .findAllById(
                                        productFilter.attributes
                                    )
                                    .toList()
                            )
                        )
                        .mapNotNull{ element ->
                            element.product?.id
                        }
                } else {
                    emptyList()
                }

            val categories: List<String> =
                if(productFilter.categories.isNotEmpty()){
                    productCategoriesRepository
                        .findAll(
                            ProductCategorySpecification(
                                categories = categoriesRepository
                                    .findAllById(
                                        productFilter.categories
                                    ).toList()
                            )
                        )
                        .mapNotNull{ element ->
                            element.product?.id
                        }
                } else {
                    emptyList()
                }

            val tags: List<String> =
                if(productFilter.criteria != null && productFilter.criteria.isNotEmpty()){
                    val criteriaTags: List<Tag> =
                        tagsRepository
                        .findByNameContainingIgnoreCase(
                            name = productFilter.criteria
                        )

                    val searched: List<ProductTag> =
                    productTagRepository
                        .findByTagIdIn(
                            criteriaTags
                                .mapNotNull { tag ->
                                    tag.id
                                }
                        )

                    searched
                        .mapNotNull{ element ->
                            element.product?.id
                        }
                } else {
                    emptyList()
                }

            val distinct: List<String> = certifications
                .union(categories)
                .union(attributes)
                .union(tags)
                .distinct()

            productsRepository
                .findAll(
                    ProductFilterSpecification(
                        ids = distinct,
                        minStock = productFilter.minimum,
                        maxStock = productFilter.maximum,
                        minBatch = productFilter.minBatch,
                        maxBatch = productFilter.maxBatch,
                        searchTerm = productFilter.criteria
                    ),
                    PageRequest.of(
                        (page - 1),
                        8
                    )
                )
        } else {
            productsRepository
                .findAll(
                    PageRequest.of(
                        (page - 1),
                        8
                    )
                )

        }
    }

    fun remove(id: String) = productsRepository.deleteById(id)

    fun favorite(id: String,
                 user: User) {
        val product: Product = find(id)

        val favorites: List<FavoriteProduct> = favoriteProductRepository.findByUser(user)

        val removable: FavoriteProduct? = favorites
            .find { element ->
                product == element.product
            }

        if(removable == null) {
            favoriteProductRepository.save(
                FavoriteProduct(
                    product = product,
                    user = user
                )
            )
        } else {
            favoriteProductRepository.deleteById(
                removable.id !!
            )
        }
    }

    fun find(id: String): Product {
        val stored: Product? = productsRepository.findById(id).unwrap()

        if(stored != null) {
            return stored
        } else {
            throw RuntimeException("Product not found for id $id")
        }
    }

    fun findByUser(user: User): List<Product> {
        return productsRepository.findByUser(user)
    }

    fun addImage(
            id: String,
            file: MultipartFile
    ) {
        val product: Product? = productsRepository.findById(id).unwrap()

        if(product != null) {
            val storable = ProductImage(
                product = product,
                fileLocation = ""
            )
            val image: ProductImage = productImageRepository.save(storable)

            val extension : String = file.extension() ?: "jpg"
            val imageFile: File = fileStorageService.store(
                    file = file,
                    filename = "${image.id}",
                    defaultExtension = extension
                )

            productImageRepository.save(
                image.copy(
                    fileLocation = imageFile
                        .toURI()
                        .toURL()
                        .toString()
                )
            )

        } else {
            throw RuntimeException("Product not found for id $id")
        }
    }

    fun removeImage(productImageId: String){
        val image: ProductImage? = productImageRepository
            .findById(productImageId)
            .unwrap()

        if(image != null){
            productImageRepository.deleteById(productImageId)
            fileStorageService.remove(
                filename = image.filename()
            )
        }
    }

    fun addTags(product: Product, tags: List<String>) {

        // Remove unwanted tags
        val toBeRemoved = product
            .productTags
            .filterNot { tag ->
                tags.contains(tag.tag?.name)
            }

        toBeRemoved.forEach { tag ->
            productTagRepository.deleteByProductAndTag(
                product = product,
                tag = tag.tag
            )
        }

        // Add new Tags
        val stored = product
            .productTags
            .mapNotNull { tag ->
                tag.tag?.name
            }

        val toBeAdded = tags.filterNot { tag ->
            stored.contains(tag)
        }

        toBeAdded.forEach { newTag ->
            val storedTag: Tag? = tagsRepository.findByName(newTag)
                ?: tagsRepository.save(
                    Tag(name = newTag)
                )

            productTagRepository.save(
                ProductTag(
                    product = product,
                    tag = storedTag
                )
            )
        }
    }

    fun addCertifications(product: Product, certifications: List<String>) {

        // Remove unwanted tags
        val toBeRemoved = product
            .productCertifications
            .filterNot { certification ->
                certifications.contains(certification.certification?.name)
            }

        toBeRemoved.forEach { certification ->
            productCertificationRepository.deleteByProductAndCertification(
                product = product,
                certification = certification.certification
            )
        }

        // Add new Tags
        val stored = product
            .productCertifications
            .mapNotNull { certification ->
                certification.certification?.name
            }

        val toBeAdded = certifications.filterNot { certification ->
            stored.contains(certification)
        }

        toBeAdded.forEach { newTag ->
            val storedCertification: CertificationTag? = certificationTagsRepository.findByName(newTag)
                ?: certificationTagsRepository.save(
                    CertificationTag(name = newTag)
                )

            productCertificationRepository.save(
                ProductCertification(
                    product = product,
                    certification = storedCertification
                )
            )
        }

    }

    fun addCategories(product: Product, categories: List<String>) {
        // Remove unwanted Categories
        product
            .productCategories
            .filterNot { attribute ->
                categories.contains(attribute.category?.name)
            }
            .forEach { category ->
                removeCategory(category.id!!)
            }

        // Add new Categories
        val stored: List<String> = product
            .productCategories
            .mapNotNull { category ->
                category.category?.name
            }

        categories
            .filterNot { category ->
                stored.contains(category)
            }
            .mapNotNull { categoryName ->
                categoriesRepository.findByName(categoryName)
            }
            .forEach { category ->
                addCategory(
                    product = product,
                    category = category
                )
            }
    }

    fun addCategory(
        productId: String,
        categoryId: String
    ) {
        val category: Category? = categoriesRepository
            .findById(categoryId)
            .unwrap()

        val product: Product? = productsRepository
            .findById(productId)
            .unwrap()

        addCategory(
            product = product,
            category = category
        )
    }

    private fun addCategory(
        product: Product?,
        category: Category?
    ){
        if(category != null && product != null){
            productCategoriesRepository.save(
                ProductCategory(
                    product = product,
                    category = category
                )
            )

            // Add all attributes for a specific category
            category
                .attributes
                .forEach { attribute ->
                    productAttributeRepository.save(
                        ProductAttribute(
                            product = product,
                            attribute = attribute
                        )
                    )
                }

        }
    }

    fun removeCategory(productCategoryId: String){
        val productCategory: ProductCategory? =
            productCategoriesRepository
            .findById(productCategoryId)
            .unwrap()

        if (productCategory != null) {
            productCategory
                .category
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

    fun inactivateAttributes(product: Product,
                             inactive: List<String>,
                             active: List<String>) {
        // Remove unwanted tags
        val attributes: List<ProductAttribute> = product.productAttributes

        attributes
            .filter { attribute ->
                inactive.contains(attribute.attribute?.name)
            }
            .forEach { attribute ->
                productAttributeRepository.save(
                    attribute.copy(
                        active = false
                    )
                )
            }

        attributes
            .filter { attribute ->
                active.contains(attribute.attribute?.name)
            }
            .forEach { attribute ->
                productAttributeRepository.save(
                    attribute.copy(
                        active = true
                    )
                )
            }
    }

}