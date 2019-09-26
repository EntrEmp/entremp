package com.entremp.core.entremp.persistence.product.attribute

import com.entremp.core.entremp.model.product.ProductAttribute
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import java.util.*

class ProductAttributeEntity(id: EntityID<UUID>)
    : AbstractEntity<ProductAttribute>(id) {

    companion object: AbstractEntityClass<ProductAttribute, ProductAttributeEntity>(
        ProductAttributeTable
    )

    var productId: UUID by ProductAttributeTable.productId
    var attributeId: UUID by ProductAttributeTable.attributeId
    var active: Boolean by ProductAttributeTable.active

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