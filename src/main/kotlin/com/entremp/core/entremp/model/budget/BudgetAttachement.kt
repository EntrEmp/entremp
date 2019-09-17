package com.entremp.core.entremp.model.budget

import com.entremp.core.entremp.model.Fileable
import org.joda.time.DateTime
import java.util.*

data class BudgetAttachement(
        val id: UUID,
        val budgetId: UUID,
        val fileLocation: String,
        val createdAt: DateTime,
        val updatedAt: DateTime?
): Fileable(fileLocation) {
        companion object {
            fun create(
                    budgetId: UUID,
                    fileLocation: String
            ): BudgetAttachement {
                    return BudgetAttachement(
                            id = UUID.randomUUID(),
                            budgetId = budgetId,
                            fileLocation = fileLocation,
                            createdAt = DateTime.now(),
                            updatedAt = null
                    )
            }
        }
}