package com.entremp.core.entremp.persistence.product

import com.entremp.core.entremp.model.product.ProductAttribute
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import java.util.*

class ProductAttributeEntity(id: EntityID<UUID>)
    : AbstractEntity<ProductAttribute>(id) {

    companion object: AbstractEntityClass<ProductAttribute, ProductAttributeEntity>(
            ProductAttributes
    )

    var productId: UUID by ProductAttributes.productId
    var attributeId: UUID by ProductAttributes.attributeId
    var active: Boolean by ProductAttributes.active

    override fun create(source: ProductAttribute): AbstractEntity<ProductAttribute> {
        return update(source)
    }

    override fun update(source: ProductAttribute): AbstractEntity<ProductAttribute> {
        productId = source.productId
        attributeId = source.attributeId
        active = source.active

        return this
    }

    override fun toDomainType(): ProductAttribute {
        return ProductAttribute(
                id = id.value,
                productId = productId,
                attributeId = attributeId,
                active = active
        )
    }
}