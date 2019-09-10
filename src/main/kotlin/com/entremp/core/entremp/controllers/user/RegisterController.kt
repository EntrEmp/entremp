package com.entremp.core.entremp.controllers.user

import com.entremp.core.entremp.api.user.UserRegistrationDTO
import com.entremp.core.entremp.data.user.UsersRepository
import com.entremp.core.entremp.model.user.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
class RegisterController(
        private val usersRepository: UsersRepository,
        private val passwordEncoder: PasswordEncoder
) {

    @RequestMapping("register", method = [RequestMethod.POST])
    fun register(@RequestBody prospect: UserRegistrationDTO){
        validateAccount(prospect)
        val user = User(
                email = prospect.email,
                passwd = passwordEncoder.encode(prospect.password),
                token = UUID.randomUUID().toString()
        )
        usersRepository.save(user)
    }

    private fun validateAccount(prospect: UserRegistrationDTO) {
        val exists: Boolean = usersRepository.existsByEmail(prospect.email)
        if(exists){
            throw RuntimeException("Account email ${prospect.email} already exists.")
        } else {
            val passwordMatch: Boolean = prospect.password == prospect.matchingPassword
            if(! passwordMatch){
                throw RuntimeException("Password and Confirmation did not match.")
            }
        }
    }

}