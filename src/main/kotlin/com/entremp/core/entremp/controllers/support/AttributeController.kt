package com.entremp.core.entremp.controllers.support

import com.entremp.core.entremp.api.EditableAttributeDTO
import com.entremp.core.entremp.data.AttributesRepository
import com.entremp.core.entremp.data.CategoriesRepository
import com.entremp.core.entremp.model.commons.Attribute
import com.entremp.core.entremp.model.commons.Category
import com.entremp.core.entremp.support.JavaSupport.unwrap
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
class AttributeController(
        private val categoriesRepository: CategoriesRepository,
        private val attributesRepository: AttributesRepository
) {
    @GetMapping("/categories/{categoryId}/attributes")
    fun allByCategory(@PathVariable categoryId: String): Iterable<Attribute> {
        val category: Category? = categoriesRepository.findById(categoryId).unwrap()

        return category?.attributes ?: emptyList()
    }

    @GetMapping("/categories/{categoryId}/attributes/{id}")
    fun show(
            @PathVariable categoryId: String,
            @PathVariable id: String
    ): Optional<Attribute> {
        return attributesRepository.findById(id)
    }

    @PostMapping("/categories/{categoryId}/attributes")
    fun save(
            @PathVariable categoryId: String,
            @RequestBody input: JsonNode
    ): Attribute {
        val category: Category? = categoriesRepository.findById(categoryId).unwrap()

        val name: String = input.get("name").asText()

        val attribute = Attribute(
            category = category,
            name = name
        )

        return attributesRepository.save(attribute)
    }

    @PutMapping("/attributes/{id}")
    fun update(
            @PathVariable id: String,
            @RequestBody attribute: EditableAttributeDTO
    ): Attribute {
        assert(attribute.id == id)
        val category: Category? = categoriesRepository.findById(attribute.categoryId).unwrap()
        val edit = Attribute(
            id = attribute.id,
            name = attribute.name,
            category = category
        )
        return attributesRepository.save(edit)
    }

    @DeleteMapping("/attributes/{id}")
    @Secured("ROLE_ADMIN")
    fun remove(
            @PathVariable categoryId: String,
            @PathVariable id: String
    ) {
        attributesRepository.deleteById(id)
    }

    @GetMapping("/attributes")
    fun all(): Iterable<Attribute> {
        return attributesRepository.findAll()
    }
}