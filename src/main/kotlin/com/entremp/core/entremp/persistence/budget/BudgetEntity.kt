package com.entremp.core.entremp.persistence.budget

import com.entremp.core.entremp.model.DeliveryTerm
import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class BudgetEntity(id: EntityID<UUID>)
    : AbstractEntity<Budget>(id){

    companion object: AbstractEntityClass<Budget, BudgetEntity>(
            Budgets
    )

    var pricingId: UUID by Budgets.pricingId
    var price: Double by Budgets.price
    var iva: Double by Budgets.iva
    var deliveryConditions: String by Budgets.deliveryConditions
    var paymentConditions: String by Budgets.paymentConditions
    var specifications: String by Budgets.specifications
    var deliveryTerm: DeliveryTerm by Budgets.deliveryTerm
    var createdAt: DateTime by Budgets.createdAt
    var updatedAt: DateTime? by Budgets.updatedAt

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