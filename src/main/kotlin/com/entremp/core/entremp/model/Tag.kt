package com.entremp.core.entremp.model

import com.entremp.core.entremp.model.product.ProductTag
import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
@EqualsAndHashCode
data class Tag(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    val id: String? = null,

    val name: String,

    @OneToMany(mappedBy = "tag")
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    val productTags: List<ProductTag> = emptyList()
)