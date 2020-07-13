package com.entremp.core.entremp.model.chat

import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.model.user.User
import com.fasterxml.jackson.annotation.JsonManagedReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class Chat(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn(name="buyer_id")
        @EqualsAndHashCode.Exclude
        val buyer: User? = null,

        @ManyToOne
        @JoinColumn(name="provider_id")
        @EqualsAndHashCode.Exclude
        val provider: User? = null,

        @OneToMany(mappedBy = "chat")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val messages: List<Message> = emptyList(),

        @OneToOne(mappedBy = "chat")
        @EqualsAndHashCode.Exclude
        val budget: Budget? = null
) {
        fun getLastMessage(): Message? = messages
                .sortedByDescending { message ->
                        message.sent.millis
                }
                .firstOrNull()
}