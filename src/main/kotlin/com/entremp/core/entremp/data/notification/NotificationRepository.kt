package com.entremp.core.entremp.data.notification

import com.entremp.core.entremp.model.notification.Notification
import com.entremp.core.entremp.model.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface NotificationRepository: JpaRepository<Notification, String>, JpaSpecificationExecutor<Notification> {
    fun findByUser(user: User?): Iterable<Notification>
}