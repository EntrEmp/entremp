package com.entremp.core.entremp.model.notification

import com.entremp.core.entremp.model.user.User
import com.fasterxml.jackson.annotation.JsonManagedReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class Notification(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    val id: String? = null,

    @ManyToOne
    @JoinColumn(name="user_id")
    val user: User? = null,

    val seen: Boolean = false,

    val type: String,

    val message: String,

    val createdAt: DateTime = DateTime.now(DateTimeZone.UTC)
){
    fun action(): String = when(type){
        "PRICING" -> "/pricings"
        "BUDGET" -> "/pricings"
        else -> "/home"
    }

    fun date(): String = createdAt.toString()
}