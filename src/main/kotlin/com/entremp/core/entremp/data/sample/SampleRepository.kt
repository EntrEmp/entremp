package com.entremp.core.entremp.data.sample

import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.sample.Sample
import org.springframework.data.repository.CrudRepository

interface SampleRepository: CrudRepository<Sample, String> {
    fun deleteByPricing(pricing: Pricing)
}