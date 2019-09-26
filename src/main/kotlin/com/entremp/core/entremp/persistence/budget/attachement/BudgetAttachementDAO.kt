package com.entremp.core.entremp.persistence.budget.attachement

import com.entremp.core.entremp.model.budget.BudgetAttachement
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class BudgetAttachementDAO: TransactionSupport() {

    fun all(): List<BudgetAttachement> = transaction {
        BudgetAttachementEntity
                .all()
                .map { entity: BudgetAttachementEntity ->
                    entity.toDomainType()
                }
    }

    fun findById(id: UUID): BudgetAttachement = transaction {
        BudgetAttachementEntity[id].toDomainType()
    }


    fun saveOrUpdate(source: BudgetAttachement): BudgetAttachement = transaction {
        BudgetAttachementEntity.saveOrUpdate(
                id = source.id,
                source = source
        )
    }

    fun delete(id: UUID) = transaction {
        BudgetAttachementEntity[id].delete()
    }

}