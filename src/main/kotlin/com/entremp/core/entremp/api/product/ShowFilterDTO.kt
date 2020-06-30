package com.entremp.core.entremp.api.product

import com.entremp.core.entremp.model.Attribute
import com.entremp.core.entremp.model.Category
import com.entremp.core.entremp.model.CertificationTag

data class ShowFilterDTO(
    val categories: List<Category> = emptyList(),

    val attributes: List<Attribute> = emptyList(),

    val certifications: List<CertificationTag> = emptyList(),

    val criteria: String = "",

    val minimum: Int? = null,
    val maximum: Int? = null,

    val minBatch: Int? = null,
    val maxBatch: Int? = null
) {
}