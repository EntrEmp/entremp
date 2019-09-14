package com.entremp.core.entremp.data.budget

import com.entremp.core.entremp.model.budget.Budget
import org.springframework.data.repository.CrudRepository

interface BudgetRepository: CrudRepository<Budget, String>