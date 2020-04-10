package com.entremp.core.entremp.data

import com.entremp.core.entremp.model.CertificationTag
import com.entremp.core.entremp.model.Tag
import org.springframework.data.repository.CrudRepository

interface CertificationTagsRepository: CrudRepository<CertificationTag, String>{
    fun findByName(name: String): CertificationTag?
}