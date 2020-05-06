package com.entremp.core.entremp.model.user

import lombok.EqualsAndHashCode
import org.joda.time.DateTime
import javax.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
@EqualsAndHashCode
data class VerificationToken(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    val id: String? = null,

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    @EqualsAndHashCode.Exclude
    val user: User,

    val token: String,

    val expiration: DateTime
)