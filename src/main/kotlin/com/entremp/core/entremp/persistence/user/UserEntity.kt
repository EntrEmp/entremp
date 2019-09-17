package com.entremp.core.entremp.persistence.user

import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class UserEntity(id: EntityID<UUID>)
    : AbstractEntity<User>(id) {

    companion object: AbstractEntityClass<User, UserEntity>(
            Users
    )

    var email: String by Users.email
    var password: String by Users.password
    var token: UUID? by Users.token
    var address: String by Users.address
    var name: String by Users.name
    var cuit: Long by Users.cuit
    var phone: String by Users.phone
    var active: Boolean by Users.active
    var createdAt: DateTime by Users.createdAt
    var updatedAt: DateTime? by Users.updatedAt

    override fun create(source: User): AbstractEntity<User> {
        return update(source)
    }

    override fun update(source: User): AbstractEntity<User> {
        email = source.email
        password = source.password
        token = source.token
        address = source.address
        name = source.name
        cuit = source.cuit
        phone = source.phone
        active = source.active
        createdAt = source.createdAt
        updatedAt = source.updatedAt

        return this
    }

    override fun toDomainType(): User {
        return User(
                id = id.value,
                email = email,
                passwd = password,
                token = token,
                name = name,
                address = address,
                phone = phone,
                active = active,
                cuit = cuit,
                createdAt = createdAt,
                updatedAt = updatedAt
        )
    }

}