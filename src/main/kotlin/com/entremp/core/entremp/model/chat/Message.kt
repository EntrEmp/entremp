package com.entremp.core.entremp.model.chat

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

        val message: String,

        val sender: String,

        val role: String,

        val sent: DateTime = DateTime.now(DateTimeZone.UTC),

        val seen: Boolean = false
)