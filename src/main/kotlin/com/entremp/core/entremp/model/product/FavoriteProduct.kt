package com.entremp.core.entremp.model.product

import com.entremp.core.entremp.model.user.User
import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class FavoriteProduct(
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
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    val user: User? = null
){
    fun productId(): String = product?.id ?: ""

}