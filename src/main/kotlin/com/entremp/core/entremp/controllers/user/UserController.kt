package com.entremp.core.entremp.controllers.user

import com.entremp.core.entremp.api.user.UserInputDTO
import com.entremp.core.entremp.controllers.Authenticated
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.service.UserService
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/users")
class UserController(
        private val userService: UserService
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

}

