package com.entremp.core.entremp.data

import com.entremp.core.entremp.model.Tag
import org.springframework.data.repository.CrudRepository

interface TagsRepository: CrudRepository<Tag, String>{
    fun findByName(name: String): Tag?
    fun findByNameContaining(name: String): List<Tag>
}