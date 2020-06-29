package com.entremp.core.entremp.service

import com.entremp.core.entremp.data.notification.NotificationFilterSpecification
import com.entremp.core.entremp.data.notification.NotificationRepository
import com.entremp.core.entremp.model.notification.Notification
import com.entremp.core.entremp.model.user.User

import com.entremp.core.entremp.support.JavaSupport.unwrap
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

class NotificationService(
    private val notificationRepository: NotificationRepository
) {

    fun getAll(): Iterable<Notification> = notificationRepository.findAll()

    fun findByUser(user: User): Iterable<Notification> {
        return notificationRepository.findByUser(user)
    }

    fun save(
        user: User,
        type: String,
        message: String
    ): Notification {
        val storable = Notification(
            user = user,
            type = type,
            message = message
        )

        return notificationRepository.save(storable)
    }

    fun markAsSeen(id: String): Notification {
        val stored: Notification? = notificationRepository
            .findById(id)
            .unwrap()

        if(stored != null){

            val storable: Notification = stored.copy(seen = true)

            return notificationRepository.save(storable)
        } else {
            throw RuntimeException("Notification not found for id $id")
        }
    }

    fun getPending(
        user: User?,
        requestedPage: Int?
    ): Page<Notification> {
        val page: Int = requestedPage ?: 1

        return notificationRepository.findAll(
            NotificationFilterSpecification(
                user = user
            ),
            PageRequest.of(
                (page - 1),
                8
            )
        )
    }
}