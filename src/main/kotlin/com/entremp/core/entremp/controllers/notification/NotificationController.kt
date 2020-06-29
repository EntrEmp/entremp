package com.entremp.core.entremp.controllers.notification

import com.entremp.core.entremp.model.notification.Notification
import com.entremp.core.entremp.service.NotificationService
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationService: NotificationService
) {

    @GetMapping("/more/{id}")
    fun seeMore(
        @PathVariable id: String,
        @RequestParam role: String,
        redirectAttributes: RedirectAttributes
    ): RedirectView {

        val notification: Notification = notificationService.markAsSeen(id)

        val action = "/${role}${notification.action()}"

        return RedirectView(action)
    }
}