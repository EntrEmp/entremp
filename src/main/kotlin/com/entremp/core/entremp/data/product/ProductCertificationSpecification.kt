package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.CertificationTag
import com.entremp.core.entremp.model.product.ProductCertification
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.*

class ProductCertificationSpecification(val certifications: List<CertificationTag>): Specification<ProductCertification> {

    override fun toPredicate(root: Root<ProductCertification>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {

        val predicates: MutableList<Predicate> = mutableListOf()

        query.distinct(true)

        if(certifications.isNotEmpty()){
            val matches: Join<ProductCertification, CertificationTag> = root
                .join("certification")


            predicates.add(
                matches.`in`(certifications)
            )
        }

        return builder.and(*predicates.toTypedArray())
    }

}