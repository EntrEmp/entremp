package com.entremp.core.entremp.model.product

import com.entremp.core.entremp.model.Tag
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class ProductTag(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    val id: String? = null,

    @ManyToOne
    @JoinColumn
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    val product: Product? = null,


    @ManyToOne
    @JoinColumn
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    val tag: Tag? = null
)