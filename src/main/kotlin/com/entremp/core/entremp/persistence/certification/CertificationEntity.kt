package com.entremp.core.entremp.persistence.certification

import com.entremp.core.entremp.model.certification.Certification
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class CertificationEntity(id: EntityID<UUID>)
    : AbstractEntity<Certification>(id) {

    companion object: AbstractEntityClass<Certification, CertificationEntity>(
            CertificationTable
    )

    var fileLocation: String by CertificationTable.fileLocation
    var userId: UUID by CertificationTable.userId
    var name: String by CertificationTable.name
    var createdAt: DateTime by CertificationTable.createdAt
    var updatedAt: DateTime? by CertificationTable.updatedAt

    override fun create(source: Certification): AbstractEntity<Certification> {
        return update(source)
    }

    override fun update(source: Certification): AbstractEntity<Certification> {
        userId = source.userId
        name = source.name
        createdAt = source.createdAt
        updatedAt = source.updatedAt

        return this
    }

    override fun toDomainType(): Certification {
        return Certification(
                id = id.value,
                userId = userId,
                name = name,
                fileLocation = fileLocation,
                createdAt = createdAt,
                updatedAt = updatedAt
        )
    }

}