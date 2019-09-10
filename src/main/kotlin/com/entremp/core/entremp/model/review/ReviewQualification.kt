package com.entremp.core.entremp.model.review

import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class ReviewQualification(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        @ManyToOne
        @JoinColumn(name="review_id")
        @JsonBackReference
        @EqualsAndHashCode.Exclude
        val review: Review? = null,

        val qualificationType: QualificationType? = null,

        val qualification: Double
)