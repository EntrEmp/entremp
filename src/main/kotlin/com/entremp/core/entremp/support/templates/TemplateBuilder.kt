package com.entremp.core.entremp.support.templates

import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory
import java.io.StringWriter

open class TemplateBuilder(
    private val templateName: String,
    private val factory: MustacheFactory
) {

    private var data: Map<String, Any?> = emptyMap()

    open fun build(): String {

        val mustache: Mustache = factory.compile(templateName)

        val writer = StringWriter()

        val data: Map<String, Any?> = data

        mustache.execute(writer, data)

        return writer.toString()
    }

    open fun data(data: Map<String, Any?>): TemplateBuilder {
        this.data = data
        return this
    }
}