package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ProductsRepository: JpaRepository<Product, String>, JpaSpecificationExecutor<Product>{
    fun findByUser(user: User): List<Product>
}