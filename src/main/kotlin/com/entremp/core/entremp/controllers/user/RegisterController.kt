package com.entremp.core.entremp.controllers.user

import com.entremp.core.entremp.api.user.ResetPasswordDTO
import com.entremp.core.entremp.api.user.UserRegistrationDTO
import com.entremp.core.entremp.data.user.PasswordResetTokenRepository
import com.entremp.core.entremp.data.user.UserAddressRepository
import com.entremp.core.entremp.data.user.UsersRepository
import com.entremp.core.entremp.data.user.VerificationTokenRepository
import com.entremp.core.entremp.events.OnRegistrationCompleteEvent
import com.entremp.core.entremp.exceptions.InvalidPassword
import com.entremp.core.entremp.exceptions.UserNotFound
import com.entremp.core.entremp.exceptions.UsernameAlreadyExists
import com.entremp.core.entremp.model.user.PasswordResetToken
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.model.user.VerificationToken
import com.entremp.core.entremp.support.EmailService
import com.entremp.core.entremp.support.EntrEmpContext
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.*
import javax.servlet.http.HttpServletRequest


@RestController
class RegisterController(
        private val usersRepository: UsersRepository,
        private val verificationTokenRepository: VerificationTokenRepository,
        private val passwordResetTokenRepository: PasswordResetTokenRepository,
        private val userAddressRepository: UserAddressRepository,
        private val eventPublisher: ApplicationEventPublisher,
        private val encoder: PasswordEncoder,
        private val mailer: EmailService,
        private val context: EntrEmpContext
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @RequestMapping("register", method = [RequestMethod.POST])
    fun register(
        @ModelAttribute prospect: UserRegistrationDTO,
        redirectAttributes: RedirectAttributes,
        request: HttpServletRequest
    ): RedirectView {

        return try {
            validateAccount(prospect)

            val registered: User = registerNewAccount(prospect)

            eventPublisher.publishEvent(OnRegistrationCompleteEvent(
                    user = registered,
                    url = request.contextPath
                )
            )

            val message = "Tu usuario se creo correctamente!"
            redirectAttributes.addFlashAttribute("success", flashSuccess(message))
            RedirectView("/verify")
        }
        catch (throwable: Throwable) {
            logger.error("Error registering user", throwable)
            redirectAttributes.addFlashAttribute("error", flashError(throwable.message))
            redirectAttributes.addFlashAttribute("prospect", prospect)
            RedirectView("/sign-up")
        }

    }

    @RequestMapping("confirm", method = [RequestMethod.GET])
    fun confirm(
        @RequestParam token: String,
        redirectAttributes: RedirectAttributes,
        request: WebRequest
    ): RedirectView {

        val verificationToken: VerificationToken? = verificationTokenRepository.findByToken(token)
        if(verificationToken == null){
            redirectAttributes.addFlashAttribute("error", flashError("Token de confirmación inválido"))
            return RedirectView("/login")
        }

        val user: User = verificationToken.user
        if(verificationToken.expiration.isBeforeNow) {
            redirectAttributes.addFlashAttribute("error", flashError("Token de confirmación expirado"))
            return RedirectView("/login")
        }

        usersRepository.save(
            user.copy(active = true)
        )

        val message = "Tu usuario se validó correctamente!"
        redirectAttributes.addFlashAttribute("success", flashSuccess(message))
        return RedirectView("/login")
    }

    @RequestMapping("recover", method = [RequestMethod.POST])
    fun recover(
        @RequestParam email: String,
        redirectAttributes: RedirectAttributes,
        request: HttpServletRequest
    ): RedirectView {

        return try {
            val user: User = usersRepository.findByEmail(email)
                ?: throw UserNotFound("No hay un usuario para el email $email")

            val token: String = UUID.randomUUID().toString()
            passwordResetTokenRepository.save(
                PasswordResetToken(
                    user = user,
                    token = token,
                    expiration = DateTime.now().plusHours(24)
                )
            )

            val server: String = context.toServerAddress()
            val url: String = "$server/reset?token=$token"

            val text =
                """
            A continuación le enviamos el link para que recupere su contraseña.
            $url
            """.trimIndent()

            mailer.sendMail(
                recipients = listOf(email),
                subject = "EntrEmp Recuperación de Contraseña",
                text = text
            )

            val message = "Se envío el mail de recuperación de contraseña!"
            redirectAttributes.addFlashAttribute("success", flashSuccess(message))
            redirectAttributes.addFlashAttribute("mail", email)
            RedirectView("/login")
        }
        catch (throwable: Throwable) {
            logger.error("Error recovering user password", throwable)

            redirectAttributes.addFlashAttribute("error", flashError(throwable.message))
            redirectAttributes.addFlashAttribute("mail", email)
            RedirectView("/recover")
        }
    }

    @RequestMapping("reset", method = [RequestMethod.POST])
    fun reset(
        @ModelAttribute resetPassword: ResetPasswordDTO,
        redirectAttributes: RedirectAttributes,
        request: HttpServletRequest
    ): RedirectView {

        val token: PasswordResetToken? =
            passwordResetTokenRepository
            .findByToken(
                resetPassword.token
            )

        if(token == null){
            redirectAttributes.addFlashAttribute("error", flashError("Token de recuperación inválido"))
            return RedirectView("/login")
        }

        if(token.expiration.isBeforeNow) {
            redirectAttributes.addFlashAttribute("error", flashError("Token de recuperación expirado"))
            return RedirectView("/login")
        }

        return try {
            validatePassword(resetPassword.password, resetPassword.confirmation)
            val user: User = token.user

            usersRepository.save(
                user.copy(
                    passwd = encoder.encode(resetPassword.password)
                )
            )
            redirectAttributes.addFlashAttribute("success", flashSuccess("Se reseteo su contraseña con exito!"))
            RedirectView("/login")
        }
        catch (throwable: Throwable) {
            logger.error("Error during new password reset", throwable)
            redirectAttributes.addFlashAttribute("error", flashError(throwable.message))
            RedirectView("/reset?token=${resetPassword.token}")
        }

    }

    private fun registerNewAccount(prospect: UserRegistrationDTO): User {
        val user = User(
            name = prospect.name,
            email = prospect.email,
            passwd = encoder.encode(prospect.password),
            phone = prospect.phone,
            cuit = prospect.cuit,
            token = UUID.randomUUID().toString()
        )

        return usersRepository.save(user)
    }

    private fun validateAccount(prospect: UserRegistrationDTO) {
        val exists: Boolean = usersRepository.existsByEmail(prospect.email)
        if(exists){
            throw UsernameAlreadyExists("An account for ${prospect.email} already exists.")
        } else {
            validatePassword(prospect.password, prospect.confirmation)
        }
    }

    private fun validatePassword(password: String, confirmation: String){
        val passwordMatch: Boolean = password == confirmation
        if(!passwordMatch){
            throw InvalidPassword("Password and confirmation do not match")
        }
    }

    private fun flashSuccess(message: String): String =
        """
            <div class="col s12">
                <div class="card teal">
                    <div class="card-content white-text">
                        <h7>$message</h7>
                    </div>
                </div>
            </div>
        """.trimIndent()

    private fun flashError(message: String?): String =
        """
            <div class="col s12">
                <div class="card materialize-red">
                    <div class="card-content white-text">
                        <h7>$message</h7>
                    </div>
                </div>
            </div>
        """.trimIndent()


}