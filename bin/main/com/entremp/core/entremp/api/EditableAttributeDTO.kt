package com.entremp.core.entremp.api

import com.fasterxml.jackson.annotation.JsonProperty

class EditableAttributeDTO(
        val id: String,
        val name: String,
        @JsonProperty("category_id")
        val categoryId: String
) {
    constructor(): this(id = "", name = "", categoryId = "")

}