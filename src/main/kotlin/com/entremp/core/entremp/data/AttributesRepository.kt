package com.entremp.core.entremp.data

import com.entremp.core.entremp.model.commons.Attribute
import org.springframework.data.repository.CrudRepository

interface AttributesRepository: CrudRepository<Attribute, String>