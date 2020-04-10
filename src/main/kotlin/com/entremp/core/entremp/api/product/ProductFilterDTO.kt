package com.entremp.core.entremp.api.product


data class ProductFilterDTO(
    val categories: List<String> = emptyList(),

    val attributes: List<String> = emptyList(),

    val certifications: List<String> = emptyList(),

    val minimum: Int? = null,
    val maximum: Int? = null,
    val minBatch: Int? = null,
    val maxBatch: Int? = null,

    val searchPage: Int = 1,

    val criteria: String? = null
){
    fun isNotEmpty(): Boolean {
        return categories.isNotEmpty() ||
                attributes.isNotEmpty() ||
                certifications.isNotEmpty() ||
                minimum != null ||
                maximum != null ||
                minBatch != null ||
                maxBatch != null ||
                (criteria != null && criteria.isNotEmpty())
    }

    fun elements(): List<String> =
        categories
        .union(attributes)
        .union(certifications)
        .distinct()
}