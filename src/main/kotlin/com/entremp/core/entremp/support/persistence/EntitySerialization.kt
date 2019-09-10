package com.entremp.core.entremp.support.persistence

import com.entremp.core.entremp.support.ObjectMapperFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object EntitySerialization {
    val objectMapper: ObjectMapper = ObjectMapperFactory.snakeCaseMapper

    fun serialize(entity: Any): String =
        objectMapper.writeValueAsString(entity)

    inline fun <reified T> deserialize(entity: String): T =
        objectMapper.readValue(entity)
}
