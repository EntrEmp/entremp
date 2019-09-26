package com.entremp.core.entremp.persistence.product

import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.product.ProductAttribute
import com.entremp.core.entremp.model.product.ProductCategory
import com.entremp.core.entremp.persistence.product.attribute.ProductAttributeEntity
import com.entremp.core.entremp.persistence.product.category.ProductCategoryEntity
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class ProductDAO: TransactionSupport() {

    fun all(): List<Product> = transaction {
        ProductEntity
            .all()
            .map { entity: ProductEntity ->
                entity.toDomainType()
            }
    }

    fun findById(id: UUID): Product = transaction {
        ProductEntity[id].toDomainType()
    }


    fun saveOrUpdate(source: Product): Product = transaction {
        ProductEntity.saveOrUpdate(
            id = source.id,
            source = source
        )
    }

    fun delete(id: UUID) = transaction {
        ProductEntity[id].delete()
    }

    fun addAttribute(source: ProductAttribute): ProductAttribute = transaction {
        ProductAttributeEntity.saveOrUpdate(
            id = source.id,
            source = source
        )
    }

    fun removeAttribute(id: UUID) = transaction {
        ProductAttributeEntity[id].delete()
    }

    fun addCategory(source: ProductCategory): ProductCategory = transaction {
        ProductCategoryEntity.saveOrUpdate(
            id = source.id,
            source = source
        )
    }

    fun removeCategory(id: UUID) = transaction {
        ProductCategoryEntity[id].delete()
    }
}