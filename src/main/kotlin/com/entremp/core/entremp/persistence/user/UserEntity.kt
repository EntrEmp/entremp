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
            UserTable
    )

    var email: String by UserTable.email
    var password: String by UserTable.password
    var token: UUID? by UserTable.token
    var address: String by UserTable.address
    var name: String by UserTable.name
    var cuit: Long by UserTable.cuit
    var phone: String by UserTable.phone
    var active: Boolean by UserTable.active
    var createdAt: DateTime by UserTable.createdAt
    var updatedAt: DateTime? by UserTable.updatedAt

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