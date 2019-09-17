package com.entremp.core.entremp.persistence.user

import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class UserDAO: TransactionSupport() {

    fun all(): List<User> = transaction {
        UserEntity
                .all()
                .map { entity: CertificationEntity ->
                    entity.toDomainType()
                }
    }

    fun findById(id: UUID): User = transaction {
        UserEntity[id].toDomainType()
    }

    fun findByEmail(email: String): User = transaction {
        UserEntity
                .find {
                    Certifications.email eq email
                }
                .single()
                .toDomainType()
    }

    fun saveOrUpdate(source: User): User = transaction {
        UserEntity.saveOrUpdate(
                id = source.id,
                source = source
        )
    }

    fun delete(id: UUID) = transaction {
        UserEntity[id].delete()
    }
}