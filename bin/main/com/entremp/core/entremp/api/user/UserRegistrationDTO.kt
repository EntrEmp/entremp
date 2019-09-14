package com.entremp.core.entremp.api.user

class UserRegistrationDTO(
        var email: String,
        var password: String,
        var matchingPassword: String
) {
    constructor(): this(email = "", password = "", matchingPassword = "")
}