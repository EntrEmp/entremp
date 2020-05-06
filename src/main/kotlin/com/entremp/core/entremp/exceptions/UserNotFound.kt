package com.entremp.core.entremp.exceptions

data class UserNotFound(override val message: String): RuntimeException(message)