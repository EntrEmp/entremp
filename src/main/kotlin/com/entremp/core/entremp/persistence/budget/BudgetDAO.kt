package com.entremp.core.entremp.persistence.budget

import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class BudgetDAO: TransactionSupport() {

    fun all(): List<Budget> = transaction {
        BudgetEntity
                .all()
                .map { entity: BudgetEntity ->
                    entity.toDomainType()
                }
    }

    fun findById(id: UUID): Budget = transaction {
        BudgetEntity[id].toDomainType()
    }


    fun saveOrUpdate(source: Budget): Budget = transaction {
        BudgetEntity.saveOrUpdate(
                id = source.id,
                source = source
        )
    }

    fun delete(id: UUID) = transaction {
        BudgetEntity[id].delete()
    }

}