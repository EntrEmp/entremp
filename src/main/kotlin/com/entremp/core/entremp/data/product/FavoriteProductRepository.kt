package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.product.FavoriteProduct
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.user.User
import org.springframework.data.repository.CrudRepository

interface FavoriteProductRepository: CrudRepository<FavoriteProduct, String>{
    fun findByUser(user: User): List<FavoriteProduct>
}