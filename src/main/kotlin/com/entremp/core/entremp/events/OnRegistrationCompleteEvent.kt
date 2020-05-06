package com.entremp.core.entremp.events

import com.entremp.core.entremp.model.user.User
import org.springframework.context.ApplicationEvent
import java.util.*

data class OnRegistrationCompleteEvent(
    val user: User,
    val url: String
): ApplicationEvent(user)