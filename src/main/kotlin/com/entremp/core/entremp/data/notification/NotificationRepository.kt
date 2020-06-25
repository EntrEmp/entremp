package com.entremp.core.entremp.data.notification

import com.entremp.core.entremp.model.notification.Notification
import com.entremp.core.entremp.model.user.User
import org.springframework.data.repository.CrudRepository

interface NotificationRepository: CrudRepository<Notification, String> {
    fun findByUser(user: User?): Iterable<Notification>
}