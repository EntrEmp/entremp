package com.entremp.core.entremp.data.user

import com.entremp.core.entremp.model.user.VerificationToken
import org.springframework.data.repository.CrudRepository

interface VerificationTokenRepository: CrudRepository<VerificationToken, String> {
    fun findByToken(token: String): VerificationToken?
}