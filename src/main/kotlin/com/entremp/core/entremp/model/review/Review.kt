package com.entremp.core.entremp.model.review

import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.model.user.User
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class Review(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn(name="buyer_id")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val buyer: User? = null,

        @ManyToOne
        @JoinColumn(name="provider_id")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val provider: User? = null,

        @OneToOne
        @JoinColumn(name="budget_id")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val budget: Budget? = null,

        @OneToMany(mappedBy = "review")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val qualifications: Set<ReviewQualification> = emptySet(),

        val comment: String
) {
    @JsonProperty
    fun delivery(): Double {
        return qualifications
                .find { item ->
                    item.qualificationType == QualificationType.DELIVERY_TERM
                }
                ?.qualification
                ?: 0.0
    }

    @JsonProperty
    fun service(): Double {
        return qualifications
                .find { item ->
                    item.qualificationType == QualificationType.SERVICE_QUALITY
                }
                ?.qualification
                ?: 0.0
    }

    @JsonProperty
    fun product(): Double {
        return qualifications
                .find { item ->
                    item.qualificationType == QualificationType.PRODUCT_QUALITY
                }
                ?.qualification
                ?: 0.0
    }

}