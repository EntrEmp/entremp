package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.Tag
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.product.ProductTag
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface ProductTagRepository: CrudRepository<ProductTag, String> {
    @Transactional
    fun deleteByProductAndTag(product: Product?, tag: Tag?)

    fun findByTagIdIn(tags: List<String>): List<ProductTag>
}