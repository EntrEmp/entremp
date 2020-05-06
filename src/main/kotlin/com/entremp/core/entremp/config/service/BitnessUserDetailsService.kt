package com.entremp.core.entremp.config.service

import com.entremp.core.entremp.data.user.UsersRepository
import com.entremp.core.entremp.model.user.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class BitnessUserDetailsService(
        private val usersRepository: UsersRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user: User? = usersRepository.findByEmail(email)
        if(user == null){
            throw RuntimeException("Username not found for $email")
        } else {
            return user
        }
    }
}