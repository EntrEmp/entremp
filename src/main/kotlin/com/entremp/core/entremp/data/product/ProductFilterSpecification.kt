package com.entremp.core.entremp.data.product

import com.entremp.core.entremp.model.product.Product

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

        return if(searchTerm != null && searchTerm.isNotEmpty()){
            val description: Path<String> = root.get<String>("description")
            val name: Path<String> = root.get<String>("name")
            val pattern = "%${searchTerm}%"

            val regexPredicate: Predicate =
            builder.or(
                builder.isTrue(
                    builder.like(
                        builder.lower(description) ,
                        pattern.toLowerCase()
                    )
                ),
                builder.isTrue(
                    builder.like(
                        builder.lower(name) ,
                        pattern.toLowerCase()
                    )
                )
            )

            if(ids.isNotEmpty()){
                // Regex + ids with numeric predicates
                val products: Path<String> = root.get<String>("id")

                builder.and(
                    builder.or(
                        builder.isTrue(
                            products.`in`(ids)
                        ),
                        builder.isTrue(
                            regexPredicate
                        )
                    ),
                    predicate
                )
            } else {
                // Regex and numeric predicates
                builder.and(
                    regexPredicate,
                    predicate
                )
            }

        } else if(ids.isNotEmpty()){
            // Ids and numeric predicates
            val products: Path<String> = root.get<String>("id")

            builder.and(
                products.`in`(ids),
                predicate
            )
        } else {
            // Just predicates
            predicate
        }

    }


}