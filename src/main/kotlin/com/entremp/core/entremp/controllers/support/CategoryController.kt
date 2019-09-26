package com.entremp.core.entremp.controllers.support

import com.entremp.core.entremp.data.CategoriesRepository
import com.entremp.core.entremp.model.commons.Category
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/categories")
class CategoryController(
        private val categoriesRepository: CategoriesRepository
) {
    @GetMapping
    fun all(): Iterable<Category> {
        return categoriesRepository.findAll()
    }

    @PostMapping
    fun save(@RequestBody input: JsonNode): Category {
        val name: String = input.get("name").asText()
        val category = Category(
            name = name
        )
        return categoriesRepository.save(category)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody category: Category): Category {
        assert(category.id == id)
        return categoriesRepository.save(category)
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    fun remove(@PathVariable id: String) {
        categoriesRepository.deleteById(id)
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: String): Optional<Category> {
        return categoriesRepository.findById(id)
    }
}