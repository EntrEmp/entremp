package com.entremp.core.entremp.events

import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.model.chat.Chat
import org.springframework.context.ApplicationEvent

data class OnBudgetAcceptEvent(
    val budget: Budget,
    val chat: Chat
): ApplicationEvent(budget)