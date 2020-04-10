package com.entremp.core.entremp.model

import com.entremp.core.entremp.model.product.ProductCertification
import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
@EqualsAndHashCode
data class CertificationTag(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    val id: String? = null,

    val name: String,

    @OneToMany(mappedBy = "certification")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    val productCertifications: List<ProductCertification> = emptyList()
)