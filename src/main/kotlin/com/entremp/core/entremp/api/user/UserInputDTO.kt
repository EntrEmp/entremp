package com.entremp.core.entremp.api.user

data class UserInputDTO(
        var email: String,
        var password: String
){
    constructor(): this(email = "", password = "")
}