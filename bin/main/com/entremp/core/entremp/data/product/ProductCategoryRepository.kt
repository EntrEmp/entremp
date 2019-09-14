package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.product.ProductCategory
import org.springframework.data.repository.CrudRepository

interface ProductCategoryRepository: CrudRepository<ProductCategory, String>