package com.entremp.core.entremp.exceptions

data class UsernameAlreadyExists(override val message: String): RuntimeException(message)