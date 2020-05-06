package com.entremp.core.entremp.data.user

import com.entremp.core.entremp.model.user.User
import org.springframework.data.repository.CrudRepository

interface UsersRepository: CrudRepository<User, String> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?
}