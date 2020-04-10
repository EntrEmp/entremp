package com.entremp.core.entremp.controllers.user

import com.entremp.core.entremp.api.user.*
import com.entremp.core.entremp.controllers.Authenticated
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.service.UserService
import org.springframework.security.access.annotation.Secured
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
        private val userService: UserService,
        private val encoder: PasswordEncoder
): Authenticated {

    @GetMapping
    fun all(): Iterable<User> {
        return userService.getAll()
    }

    @PostMapping
    fun save(@RequestBody input: UserInputDTO): User {
        return userService.register(
                input.email,
                input.password
        )
    }

    @PutMapping("/{id}")
    fun update(
            @PathVariable id: String,
            @RequestBody user: User
    ): User {
        assert(user.id == id)

        return userService.updateUserData(
                id,
                user.password,
                user.cuit,
                user.address,
                user.phone
        )
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    fun remove(@PathVariable id: String) {
        userService.remove(id)
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: String): User {
        return userService.find(id)
    }


    @PostMapping("/{id}/certification")
    fun addCertification(
            @PathVariable id: String,
            @RequestParam file: MultipartFile,
            @RequestParam name: String
    ): User {
        val auth: User? = getAuthUser()
        assert(id == auth?.id)

        userService.addCertification(id, name, file)

        return userService.find(id)
    }

    @DeleteMapping("/{id}/certification/{certificationId}")
    fun removeCertification(
            @PathVariable id: String,
            @PathVariable certificationId: String
    ){
        val auth: User? = getAuthUser()
        assert(id == auth?.id)

        userService.removeCertification(certificationId)
    }

    @RequestMapping("/{id}/data", method = [RequestMethod.POST])
    fun updateData(
        @PathVariable id: String,
        @ModelAttribute data: EditUserDataDTO,
        redirectAttributes: RedirectAttributes
    ): RedirectView {

        userService.updateData(id, data)

        redirectAttributes.addFlashAttribute("success", flashSuccess())

        return RedirectView("/web/${data.role}/profile/$id/edit")
    }

    @RequestMapping("/{id}/password", method = [RequestMethod.POST])
    fun updatePassword(
        @PathVariable id: String,
        @ModelAttribute data: EditUserPasswordDTO,
        redirectAttributes: RedirectAttributes
    ): RedirectView {

        val user: User = userService.find(id)

        val isCurrentOk: Boolean = encoder.matches(
            data.current,
            user.password
        )

        val isNewConfirmed = data.password == data.confirmation

        return if(isCurrentOk && isNewConfirmed){

            userService.updatePassword(
                id = id,
                encoded = encoder.encode(data.password)
            )

            redirectAttributes.addFlashAttribute("success", flashSuccess())

            return RedirectView("/web/${data.role}/profile/$id/edit")
        } else {
            redirectAttributes.addFlashAttribute("error", flushError())

            return RedirectView("/web/${data.role}/profile/$id/edit")

        }

    }

    @RequestMapping("/{id}/addresses", method = [RequestMethod.POST])
    fun addAddress(
        @PathVariable id: String,
        @ModelAttribute data: UserAddressDTO,
        redirectAttributes: RedirectAttributes
    ): RedirectView {

        userService.addAddress(id, data)

        redirectAttributes.addFlashAttribute("success", flashSuccess())

        return RedirectView("/web/${data.role}/profile/$id/edit")
    }

    @RequestMapping("/{id}/delete-addresses", method = [RequestMethod.POST])
    fun removeAddress(
        @PathVariable id: String,
        @ModelAttribute data: RemoveUserAddressDTO,
        redirectAttributes: RedirectAttributes
    ): RedirectView {

        userService.removeAddress(id, data.addressId)

        redirectAttributes.addFlashAttribute("success", flashSuccess())

        return RedirectView("/web/${data.role}/profile/$id/edit")
    }

    @RequestMapping("/{id}/addresses/{addressId}", method = [RequestMethod.POST])
    fun editAddress(
        @PathVariable id: String,
        @PathVariable addressId: String,
        @ModelAttribute data: UserAddressDTO,
        redirectAttributes: RedirectAttributes
    ): RedirectView {

        userService.editAddress(id, addressId, data)

        redirectAttributes.addFlashAttribute("success", flashSuccess())

        return RedirectView("/web/${data.role}/profile/$id/edit")
    }

    @PostMapping("/{id}/image")
    fun updateProfilePic(@PathVariable id: String,
                         @RequestParam image: MultipartFile,
                         @RequestParam role: String,
                         redirectAttributes: RedirectAttributes
    ): RedirectView {
        userService.addImage(id, image)

        redirectAttributes.addFlashAttribute("success", flashSuccess())

        return RedirectView("/web/$role/profile/$id/edit")
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

    private fun flushError(): String =
        """
            <div class="col s12">
                <div class="card teal">
                    <div class="card-content white-text">
                        <h7>No se pudo efectuar la operación deseada!</h7>
                    </div>
                </div>
            </div>
        """.trimIndent()
}

