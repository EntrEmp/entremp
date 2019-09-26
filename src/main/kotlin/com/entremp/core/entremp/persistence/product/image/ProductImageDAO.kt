package com.entremp.core.entremp.persistence.product.image

import com.entremp.core.entremp.model.product.ProductImage
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class ProductImageDAO: TransactionSupport() {

    fun all(): List<ProductImage> = transaction {
        ProductImageEntity
            .all()
            .map { entity: ProductImageEntity ->
                entity.toDomainType()
            }
    }

    fun findById(id: UUID): ProductImage = transaction {
        ProductImageEntity[id].toDomainType()
    }


    fun saveOrUpdate(source: ProductImage): ProductImage = transaction {
        ProductImageEntity.saveOrUpdate(
            id = source.id,
            source = source
        )
    }
}