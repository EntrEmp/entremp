package com.entremp.core.entremp.persistence.commons

import com.entremp.core.entremp.model.commons.Category
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class CategoryDAO: TransactionSupport() {
    fun all(): List<Category> = transaction {
        CategoryEntity
            .all()
            .map { entity: CategoryEntity ->
                entity.toDomainType()
            }
    }

    fun findById(id: UUID): Category = transaction {
        CategoryEntity[id].toDomainType()
    }


    fun saveOrUpdate(source: Category): Category = transaction {
        CategoryEntity.saveOrUpdate(
            id = source.id,
            source = source
        )
    }
}