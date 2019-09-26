package com.entremp.core.entremp.persistence.commons

import com.entremp.core.entremp.model.commons.Category
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import java.util.*

class CategoryEntity(id: EntityID<UUID>)
    : AbstractEntity<Category>(id) {

    companion object: AbstractEntityClass<Category, CategoryEntity>(
        CategoryTable
    )

    var name: String by CategoryTable.name

    override fun create(source: Category): AbstractEntity<Category> {
        return update(source)
    }

    override fun update(source: Category): AbstractEntity<Category> {
        name = source.name

        return this
    }

    override fun toDomainType(): Category {
        return Category(
            id = id.value,
            name = name
        )
    }
}