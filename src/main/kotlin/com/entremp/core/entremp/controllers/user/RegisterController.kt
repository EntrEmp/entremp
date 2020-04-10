package com.entremp.core.entremp.controllers.user

import com.entremp.core.entremp.api.user.UserRegistrationDTO
import com.entremp.core.entremp.data.user.UserAddressRepository
import com.entremp.core.entremp.data.user.UsersRepository
import com.entremp.core.entremp.model.user.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.*


@RestController
class RegisterController(
        private val usersRepository: UsersRepository,
        private val userAddressRepository: UserAddressRepository,
        private val encoder: PasswordEncoder
) {

    @RequestMapping("register", method = [RequestMethod.POST])
    fun register(
        @ModelAttribute prospect: UserRegistrationDTO,
        redirectAttributes: RedirectAttributes
    ): RedirectView {
        validateAccount(prospect)
        val user = User(
            name = prospect.name,
            email = prospect.email,
            passwd = encoder.encode(prospect.password),
            phone = prospect.phone,
            cuit = prospect.cuit,
            token = UUID.randomUUID().toString()
        )

        usersRepository.save(user)

        redirectAttributes.addFlashAttribute("success", flashSuccess())

        return RedirectView("/web/login")
    }

    private fun validateAccount(prospect: UserRegistrationDTO) {
        val exists: Boolean = usersRepository.existsByEmail(prospect.email)
        if(exists){
            throw RuntimeException("Account email ${prospect.email} already exists.")
        } else {
            val passwordMatch: Boolean = prospect.password == prospect.confirmation
            if(!passwordMatch){
                throw RuntimeException("Password and Confirmation did not match.")
            }
        }
    }

    private fun flashSuccess(): String =
        """
            <div class="col s12">
                <div class="card teal">
                    <div class="card-content white-text">
                        <h7>Tu usuario se creo correctamente!</h7>
                    </div>
                </div>
            </div>
        """.trimIndent()


}