package com.entremp.core.entremp.persistence.budget.attachement

import com.entremp.core.entremp.model.budget.BudgetAttachement
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class BudgetAttachementEntity(id: EntityID<UUID>)
    : AbstractEntity<BudgetAttachement>(id){

    companion object: AbstractEntityClass<BudgetAttachement, BudgetAttachementEntity> (
        BudgetAttachementTable
    )

    var fileLocation: String by BudgetAttachementTable.fileLocation
    var budgetId: UUID by BudgetAttachementTable.budgetId
    var createdAt: DateTime by BudgetAttachementTable.createdAt
    var updatedAt: DateTime? by BudgetAttachementTable.updatedAt

    override fun create(source: BudgetAttachement): AbstractEntity<BudgetAttachement> {
        return update(source)
    }

    override fun update(source: BudgetAttachement): AbstractEntity<BudgetAttachement> {
        fileLocation = source.fileLocation
        budgetId = source.budgetId
        createdAt = source.createdAt
        updatedAt = source.updatedAt

        return this
    }

    override fun toDomainType(): BudgetAttachement {
        return BudgetAttachement(
                id = id.value,
                budgetId = budgetId,
                fileLocation = fileLocation,
                createdAt = createdAt,
                updatedAt = updatedAt
        )
    }


}