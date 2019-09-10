package com.entremp.core.entremp.model.user

import com.entremp.core.entremp.model.Fileable
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class Certification(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn(name="user_id")
        @EqualsAndHashCode.Exclude
        val user: User? = null,

        val name: String,

        private val fileLocation: String
): Fileable(fileLocation)