package com.entremp.core.entremp.model.user

import com.entremp.core.entremp.model.chat.Chat
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.product.FavoriteProduct
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.review.Review
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@EqualsAndHashCode
data class User(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        val id: String? = null,

        val email: String,

        @Column(name = "password")
        val passwd: String,

        @JsonIgnore
        val token: String,
        val active: Boolean = false,

        val name: String = "",
        val address: String = "",
        val phone: Long = Long.MIN_VALUE,
        val cuit: Long = Long.MIN_VALUE,

        @OneToMany(mappedBy = "user")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val certifications: List<Certification> = emptyList(),

        @OneToMany(mappedBy = "user")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val images: List<UserImage> = emptyList(),

        @OneToMany(mappedBy = "user")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val addresses: List<UserAddress> = emptyList(),

        @OneToMany(mappedBy = "user")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val products: List<Product> = emptyList(),

        @OneToMany(mappedBy = "user")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val favoriteProducts: List<FavoriteProduct> = emptyList(),

        @OneToMany(mappedBy = "buyer")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val applied: List<Pricing> = emptyList(),

        @OneToMany(mappedBy = "provider")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val requested: List<Pricing> = emptyList(),

        @OneToMany(mappedBy = "provider")
        @JsonManagedReference
        @EqualsAndHashCode.Exclude
        val reviews: List<Review> = emptyList(),

        @OneToMany(mappedBy = "buyer")
        @JsonIgnore
        @EqualsAndHashCode.Exclude
        val buyerChats: List<Chat> = emptyList(),

        @OneToMany(mappedBy = "provider")
        @JsonIgnore
        @EqualsAndHashCode.Exclude
        val providerChats: List<Chat> = emptyList()
): UserDetails {

        @JsonIgnore
        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
                val authorities = mutableListOf<GrantedAuthority>()
                authorities.add(
                        SimpleGrantedAuthority("ROLE_USER")
                )

                if (this.email.contains("franco.testori")) {
                        authorities.add(
                                SimpleGrantedAuthority("ROLE_ADMIN")
                        )
                }
                return authorities
        }

        @JsonIgnore
        override fun isEnabled(): Boolean = true

        override fun getUsername(): String = email

        @JsonIgnore
        override fun isCredentialsNonExpired(): Boolean = true

        @JsonIgnore
        override fun getPassword(): String = passwd

        @JsonIgnore
        override fun isAccountNonExpired(): Boolean = true

        @JsonIgnore
        override fun isAccountNonLocked(): Boolean = true

        @JsonProperty
        fun chats(): List<String> = (buyerChats + providerChats).mapNotNull { item ->
                item.id
        }

        @JsonProperty
        fun deliveryScore(): Double {
                return reviews
                        .mapNotNull { review ->
                                review.delivery()
                        }
                        .average()
        }

        @JsonProperty
        fun serviceScore(): Double {
                return reviews
                        .mapNotNull { review ->
                                review.service()
                        }
                        .average()
        }

        @JsonProperty
        fun productScore(): Double {
                return reviews
                        .mapNotNull { review ->
                                review.product()
                        }
                        .average()
        }

}