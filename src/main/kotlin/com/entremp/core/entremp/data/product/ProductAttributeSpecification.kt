package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.Attribute
import com.entremp.core.entremp.model.CertificationTag
import com.entremp.core.entremp.model.product.ProductAttribute
import com.entremp.core.entremp.model.product.ProductCertification
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.*

class ProductAttributeSpecification(val attributes: List<Attribute>): Specification<ProductAttribute> {

    override fun toPredicate(root: Root<ProductAttribute>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {

        val predicates: MutableList<Predicate> = mutableListOf()

        query.distinct(true)

        if(attributes.isNotEmpty()){
            val matches: Join<ProductAttribute, Attribute> = root
                .join("attribute")


            predicates.add(
                matches.`in`(attributes)
            )
        }

        return builder.and(*predicates.toTypedArray())
    }

}