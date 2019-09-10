package com.entremp.core.entremp.api.chat

class MessageDTO(
        val message: String
) {
    constructor(): this(
            message = ""
    )
}