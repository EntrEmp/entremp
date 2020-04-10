package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.Category
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.product.ProductCategory
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface ProductCategoryRepository:
    CrudRepository<ProductCategory, String>,
    JpaSpecificationExecutor<ProductCategory>{
    @Transactional
    fun deleteByProductAndCategory(product: Product?, category: Category?)
}