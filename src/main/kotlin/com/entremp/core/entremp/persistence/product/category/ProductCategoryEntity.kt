package com.entremp.core.entremp.persistence.product.category

import com.entremp.core.entremp.model.product.ProductCategory
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import java.util.*

class ProductCategoryEntity(id: EntityID<UUID>)
    : AbstractEntity<ProductCategory>(id) {

    companion object: AbstractEntityClass<ProductCategory, ProductCategoryEntity>(
        ProductCategoryTable
    )

    var productId: UUID by ProductCategoryTable.productId
    var categoryId: UUID by ProductCategoryTable.categoryId

    override fun create(source: ProductCategory): AbstractEntity<ProductCategory> {
        return update(source)
    }

    override fun update(source: ProductCategory): AbstractEntity<ProductCategory> {
        productId = source.productId
        categoryId = source.categoryId
        return this
    }

    override fun toDomainType(): ProductCategory {
        return ProductCategory(
            id = id.value,
            productId = productId,
            categoryId = categoryId
        )
    }
}