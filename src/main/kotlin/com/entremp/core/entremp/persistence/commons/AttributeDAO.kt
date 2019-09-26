package com.entremp.core.entremp.persistence.commons

import com.entremp.core.entremp.model.commons.Attribute
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class AttributeDAO: TransactionSupport() {
    fun all(): List<Attribute> = transaction {
        AttributeEntity
            .all()
            .map { entity: AttributeEntity ->
                entity.toDomainType()
            }
    }

    fun findById(id: UUID): Attribute = transaction {
        AttributeEntity[id].toDomainType()
    }


    fun saveOrUpdate(source: Attribute): Attribute = transaction {
        AttributeEntity.saveOrUpdate(
            id = source.id,
            source = source
        )
    }
}