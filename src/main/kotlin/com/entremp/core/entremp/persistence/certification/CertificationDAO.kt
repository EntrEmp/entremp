package com.entremp.core.entremp.persistence.certification

import com.entremp.core.entremp.model.certification.Certification
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class CertificationDAO: TransactionSupport() {

    fun all(): List<Certification> = transaction {
        CertificationEntity
                .all()
                .map { entity: CertificationEntity ->
                    entity.toDomainType()
                }
    }

    fun findById(id: UUID): Certification = transaction {
        CertificationEntity[id].toDomainType()
    }


    fun saveOrUpdate(source: Certification): Certification = transaction {
        CertificationEntity.saveOrUpdate(
                id = source.id,
                source = source
        )
    }

    fun delete(id: UUID) = transaction {
        CertificationEntity[id].delete()
    }
}