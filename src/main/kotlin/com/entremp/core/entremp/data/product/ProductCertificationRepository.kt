package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.CertificationTag
import com.entremp.core.entremp.model.Tag
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.product.ProductCertification
import com.entremp.core.entremp.model.product.ProductTag
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface ProductCertificationRepository:
    CrudRepository<ProductCertification, String>,
    JpaSpecificationExecutor<ProductCertification> {

    @Transactional
    fun deleteByProductAndCertification(product: Product?, certification: CertificationTag?)
}