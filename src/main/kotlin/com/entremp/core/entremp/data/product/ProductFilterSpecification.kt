package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.product.ProductAttribute
import com.entremp.core.entremp.model.product.ProductCategory
import com.entremp.core.entremp.model.product.ProductCertification

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.*

data class ProductFilterSpecification(
    val ids: List<String>,
    val minStock: Int?,
    val maxStock: Int?,
    val minBatch: Int?,
    val maxBatch: Int?,
    val searchTerm: String?
): Specification<Product> {

    override fun toPredicate(root: Root<Product>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {

        val predicates: MutableList<Predicate> = mutableListOf()
        query.distinct(true)

        if(ids.isNotEmpty()){
            val products: Path<String> = root.get<String>("id")
            predicates.add(
                products.`in`(ids)
            )
        }

        if(searchTerm != null && searchTerm.isNotEmpty()){
            val description: Path<String> = root.get<String>("description")
            val name: Path<String> = root.get<String>("name")
            predicates.add(
                builder.or(
                    builder.like(
                        description , "%$searchTerm%"
                    ),
                    builder.like(
                        name , "%$searchTerm%"
                    )
                )
            )
        }

        if(minStock != null && minStock > 0){
            val minValues: Path<Int> = root.get<Int>("minimum")
            predicates.add(
                builder.greaterThanOrEqualTo(
                    minValues , minStock
                )
            )
        }

        if(maxStock != null && maxStock > 0){
            val maxValues: Path<Int> = root.get<Int>("maximum")
            predicates.add(
                builder.lessThanOrEqualTo(
                    maxValues , maxStock
                )
            )
        }

        if(minBatch != null && minBatch > 0){
            val batchSize: Path<Int> = root.get<Int>("batch_size")
            predicates.add(
                builder.greaterThanOrEqualTo(
                    batchSize , minBatch
                )
            )
        }

        if(maxBatch != null && maxBatch > 0){
            val batchSize: Path<Int> = root.get<Int>("batch_size")
            predicates.add(
                builder.lessThanOrEqualTo(
                    batchSize , maxBatch
                )
            )
        }

        val predicate: Predicate? = builder.and(*predicates.toTypedArray())

        return predicate
    }

}