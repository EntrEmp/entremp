package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.Attribute
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.product.ProductAttribute
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface ProductAttributeRepository: CrudRepository<ProductAttribute, String> {
    @Transactional
    fun deleteByProductAndAttribute(product: Product?, attribute: Attribute?)
}