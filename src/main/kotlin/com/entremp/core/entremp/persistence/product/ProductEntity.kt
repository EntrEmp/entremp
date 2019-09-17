package com.entremp.core.entremp.persistence.product

import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class ProductEntity(id: EntityID<UUID>)
    : AbstractEntity<Product>(id) {

    companion object: AbstractEntityClass<Product, ProductEntity>(
            Products
    )

    var userId: UUID by Products.userId
    var name: String by Products.name
    var minimum: Int by Products.minimum
    var maximum: Int by Products.maximum
    var batchSize: Int by Products.batchSize
    var description: String by Products.description
    var createdAt: DateTime by Products.createdAt
    var updatedAt: DateTime? by Products.updatedAt

    override fun create(source: Product): AbstractEntity<Product> {
        return update(source)
    }

    override fun update(source: Product): AbstractEntity<Product> {
        userId = source.userId
        name = source.name
        minimum = source.minimum
        maximum = source.maximum
        batchSize = source.batchSize
        description = source.description
        createdAt = source.createdAt
        updatedAt = source.updatedAt

        return this
    }

    override fun toDomainType(): Product {
        return Product(
                id = id.value,
                userId = userId,
                name = name,
                minimum = minimum,
                maximum = maximum,
                batchSize = batchSize,
                description = description,
                createdAt = createdAt,
                updatedAt = updatedAt
        )
    }
}