package com.entremp.core.entremp.persistence.commons

import com.entremp.core.entremp.model.commons.Attribute
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import java.util.*

class AttributeEntity(id: EntityID<UUID>)
    : AbstractEntity<Attribute>(id) {

    companion object: AbstractEntityClass<Attribute, AttributeEntity>(
        AttributeTable
    )

    var categoryId: UUID by AttributeTable.categoryId
    var name: String by AttributeTable.name

    override fun create(source: Attribute): AbstractEntity<Attribute> {
        return update(source)
    }

    override fun update(source: Attribute): AbstractEntity<Attribute> {
        categoryId = source.categoryId
        name = source.name

        return this
    }

    override fun toDomainType(): Attribute {
        return Attribute(
            id = id.value,
            categoryId = categoryId,
            name = name
        )
    }
}