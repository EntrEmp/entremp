package com.entremp.core.entremp.data

import com.entremp.core.entremp.model.Category
import org.springframework.data.repository.CrudRepository

interface CategoriesRepository: CrudRepository<Category, String>