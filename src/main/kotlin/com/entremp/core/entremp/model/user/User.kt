package com.entremp.core.entremp.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import org.joda.time.DateTime
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

data class User(
        val id: UUID,
        val email: String,
        val passwd: String,
        /**
         * Authentication token used for registration
         */
        val token: UUID? = null,
        val name: String,
        val address: String,
        val phone: String,
        val active: Boolean,
        val cuit: Long,
        val createdAt: DateTime,
        val updatedAt: DateTime? = null
): UserDetails {

        companion object {
                fun create(
                        email: String,
                        password: String,
                        name: String,
                        cuit: Long
                ): User {
                        return User(
                                id = UUID.randomUUID(),
                                token = UUID.randomUUID(),
                                email = email,
                                passwd = password,
                                name = name,
                                cuit = cuit,
                                phone = "",
                                address = "",
                                active = false,
                                createdAt = DateTime.now()
                        )
                }
        }

        @JsonIgnore
        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
                val authorities = mutableListOf<GrantedAuthority>()
                authorities.add(
                        SimpleGrantedAuthority("ROLE_USER")
                )

                if (this.email.contains("franco.testori")) {
                        authorities.add(
                                SimpleGrantedAuthority("ROLE_ADMIN")
                        )
                }
                return authorities
        }

        @JsonIgnore
        override fun isEnabled(): Boolean = true

        override fun getUsername(): String = email

        @JsonIgnore
        override fun isCredentialsNonExpired(): Boolean = true

        @JsonIgnore
        override fun getPassword(): String = passwd

        @JsonIgnore
        override fun isAccountNonExpired(): Boolean = true

        @JsonIgnore
        override fun isAccountNonLocked(): Boolean = true

}