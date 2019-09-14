package com.entremp.core.entremp.data.pricing

import com.entremp.core.entremp.model.pricing.PricingAttachement
import org.springframework.data.repository.CrudRepository

interface PricingAttachementRepository: CrudRepository<PricingAttachement, String>