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
            ProductTable
    )

    var userId: UUID by ProductTable.userId
    var name: String by ProductTable.name
    var minimum: Int by ProductTable.minimum
    var maximum: Int by ProductTable.maximum
    var batchSize: Int by ProductTable.batchSize
    var description: String by ProductTable.description
    var createdAt: DateTime by ProductTable.createdAt
    var updatedAt: DateTime? by ProductTable.updatedAt

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