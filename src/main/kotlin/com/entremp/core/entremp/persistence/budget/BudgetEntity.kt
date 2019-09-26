package com.entremp.core.entremp.persistence.budget

import com.entremp.core.entremp.model.commons.DeliveryTerm
import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class BudgetEntity(id: EntityID<UUID>)
    : AbstractEntity<Budget>(id){

    companion object: AbstractEntityClass<Budget, BudgetEntity>(
            BudgetTable
    )

    var pricingId: UUID by BudgetTable.pricingId
    var price: Double by BudgetTable.price
    var iva: Double by BudgetTable.iva
    var deliveryConditions: String by BudgetTable.deliveryConditions
    var paymentConditions: String by BudgetTable.paymentConditions
    var specifications: String by BudgetTable.specifications
    var deliveryTerm: DeliveryTerm by BudgetTable.deliveryTerm
    var createdAt: DateTime by BudgetTable.createdAt
    var updatedAt: DateTime? by BudgetTable.updatedAt

    override fun create(source: Budget): AbstractEntity<Budget> {
        return update(source)
    }

    override fun update(source: Budget): AbstractEntity<Budget> {
        pricingId = source.pricingId
        price = source.price
        iva = source.iva
        deliveryConditions = source.deliveryConditions
        paymentConditions = source.paymentConditions
        specifications = source.specifications
        deliveryTerm = source.deliveryTerm
        createdAt = source.createdAt
        updatedAt = source.updatedAt

        return this
    }

    override fun toDomainType(): Budget {
        return Budget(
                id = id.value,
                pricingId = pricingId,
                price = price,
                iva = iva,
                deliveryConditions = deliveryConditions,
                paymentConditions = paymentConditions,
                specifications = specifications,
                deliveryTerm = deliveryTerm,
                createdAt = createdAt,
                updatedAt = updatedAt
        )
    }
}