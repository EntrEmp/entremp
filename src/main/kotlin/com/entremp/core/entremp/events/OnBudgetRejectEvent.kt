package com.entremp.core.entremp.events

import com.entremp.core.entremp.model.budget.Budget
import org.springframework.context.ApplicationEvent

data class OnBudgetRejectEvent(
    val budget: Budget
): ApplicationEvent(budget)