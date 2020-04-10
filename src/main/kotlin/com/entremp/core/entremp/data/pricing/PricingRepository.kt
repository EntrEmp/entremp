package com.entremp.core.entremp.data.pricing

import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.pricing.PricingStatus
import com.entremp.core.entremp.model.user.User
import org.springframework.data.repository.CrudRepository

interface PricingRepository: CrudRepository<Pricing, String> {
    fun findByBuyerOrProvider(buyer: User?, provider: User?): Iterable<Pricing>
    fun findByBuyer(buyer: User?): Iterable<Pricing>
    fun findByProvider(provider: User?): Iterable<Pricing>
    fun findByStatus(status:PricingStatus): Iterable<Pricing>
    fun findByBuyerAndStatus(buyer: User?, status: PricingStatus): Iterable<Pricing>
    fun findByProviderAndStatus(provider: User?, status: PricingStatus): Iterable<Pricing>
}