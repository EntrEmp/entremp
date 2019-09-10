package com.entremp.core.entremp.controllers

import com.entremp.core.entremp.model.user.User
import org.springframework.security.core.context.SecurityContextHolder

interface Authenticated {

    fun getAuthUser(): User? {
        val auth: Any = SecurityContextHolder
                .getContext()
                .authentication
                .principal

        return if(auth is User){
            auth
        } else {
            null
        }
    }
}