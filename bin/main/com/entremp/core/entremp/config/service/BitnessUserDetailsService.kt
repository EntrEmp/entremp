package com.entremp.core.entremp.config.service

import com.entremp.core.entremp.data.user.UsersRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class BitnessUserDetailsService(
        private val usersRepository: UsersRepository
): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        return usersRepository.findByEmail(email)
    }
}