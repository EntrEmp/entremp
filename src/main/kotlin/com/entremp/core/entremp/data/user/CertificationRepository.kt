package com.entremp.core.entremp.data.user

import com.entremp.core.entremp.model.user.Certification
import org.springframework.data.repository.CrudRepository

interface CertificationRepository: CrudRepository<Certification, String>