package com.entremp.core.entremp.data.user

import com.entremp.core.entremp.model.user.PasswordResetToken
import org.springframework.data.repository.CrudRepository

interface PasswordResetTokenRepository: CrudRepository<PasswordResetToken, String> {
    fun findByToken(token: String): PasswordResetToken?
}