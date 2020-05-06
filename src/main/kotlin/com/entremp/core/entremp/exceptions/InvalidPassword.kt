package com.entremp.core.entremp.exceptions

data class InvalidPassword(override val message: String): RuntimeException(message)