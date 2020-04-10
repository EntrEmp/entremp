package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.Category
import com.entremp.core.entremp.model.CertificationTag
import com.entremp.core.entremp.model.product.ProductCategory
import com.entremp.core.entremp.model.product.ProductCertification
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.*

class ProductCategorySpecification(val categories: List<Category>): Specification<ProductCategory> {

    override fun toPredicate(root: Root<ProductCategory>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {

        val predicates: MutableList<Predicate> = mutableListOf()

        query.distinct(true)

        if(categories.isNotEmpty()){
            val matches: Join<ProductCategory, Category> = root
                .join("category")

            predicates.add(
                matches.`in`(categories)
            )
        }

        return builder.and(*predicates.toTypedArray())
    }

}