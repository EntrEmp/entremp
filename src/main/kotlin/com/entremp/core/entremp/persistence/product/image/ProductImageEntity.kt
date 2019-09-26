package com.entremp.core.entremp.persistence.product.image

import com.entremp.core.entremp.model.product.ProductImage
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class ProductImageEntity(id: EntityID<UUID>)
    : AbstractEntity<ProductImage>(id){

    companion object: AbstractEntityClass<ProductImage, ProductImageEntity>(
        ProductImageTable
    )

    var productId: UUID by ProductImageTable.productId
    var fileLocation: String by ProductImageTable.fileLocation
    var createdAt: DateTime by ProductImageTable.createdAt
    var updatedAt: DateTime? by ProductImageTable.updatedAt

    override fun create(source: ProductImage): AbstractEntity<ProductImage> {
        return update(source)
    }

    override fun update(source: ProductImage): AbstractEntity<ProductImage> {
        productId = source.productId
        fileLocation = source.fileLocation
        createdAt = source.createdAt
        updatedAt = source.updatedAt

        return this
    }

    override fun toDomainType(): ProductImage {
        return ProductImage(
                id = id.value,
                productId = productId,
                fileLocation = fileLocation,
                createdAt = createdAt,
                updatedAt = updatedAt
        )
    }
}