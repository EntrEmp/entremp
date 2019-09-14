package com.entremp.core.entremp.model.chat

import com.entremp.core.entremp.model.user.User
import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class Message(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn(name="chat_id")
        @JsonBackReference
        val chat: Chat? = null,

        @ManyToOne
        @JoinColumn(name="sender_id")
        val sender: User? = null,

        @ManyToOne
        @JoinColumn(name="receiver_id")
        val receiver: User? = null,

        val message: String,

        val sent: DateTime = DateTime.now(DateTimeZone.UTC),

        val seen: Boolean = false
)