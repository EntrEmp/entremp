package com.entremp.core.entremp.controllers

import com.entremp.core.entremp.support.EmailService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mail")
class EmailSupportController(
        private val emailService: EmailService
) {

    @GetMapping
    fun send(){
        emailService.sendMail(
                recipients = listOf(
                        "franco.testori@ing.austral.edu.ar",
                        "franco.testori@despegar.com"
                ),
                subject = "TEST",
                text = "TEST"
        )
    }
}