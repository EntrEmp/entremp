package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.product.ProductImage
import org.springframework.data.repository.CrudRepository

interface ProductImageRepository: CrudRepository<ProductImage, String>{}