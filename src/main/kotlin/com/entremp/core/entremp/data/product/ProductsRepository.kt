package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.product.Product
import org.springframework.data.repository.CrudRepository

interface ProductsRepository: CrudRepository<Product, String>