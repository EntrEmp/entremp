package com.entremp.core.entremp.model.budget

import com.entremp.core.entremp.model.Fileable
import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class BudgetAttachement(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val budget: Budget? = null,

        private val fileLocation: String
): Fileable(fileLocation)