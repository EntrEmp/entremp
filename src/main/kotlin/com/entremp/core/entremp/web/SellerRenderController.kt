package com.entremp.core.entremp.web

import com.entremp.core.entremp.model.Attribute
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.user.Certification
import com.entremp.core.entremp.service.ProductService
import com.entremp.core.entremp.support.templates.TemplateBuilder
import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import java.util.jar.Attributes

@Controller
@RequestMapping("/web/seller")
class SellerRenderController(
    private val productService: ProductService
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private val factory: MustacheFactory = DefaultMustacheFactory()

    /**
     * Home Routes
     */
    @RequestMapping(
        "/home",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun home(): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val data: Map<String, Any?> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications
        )

        val body = template(
            resource = "templates/common/home.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    /**
     * Product Routes
     */
    @RequestMapping(
        "/products",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun products(): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val products: List<Product> = productService.getAll().toList()

        val data: Map<String, Any?> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications,

            "products" to products
        )

        val body = template(
            resource = "templates/products/index.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/products/create",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun createProduct(): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val data: Map<String, Any?> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications,

            "attributes" to listOf(
                Attribute(name = "Apple"),
                Attribute(name = "Microsoft"),
                Attribute(name = "Google")
            ),
            "tags" to listOf(
                Attribute(name = "Apple"),
                Attribute(name = "Microsoft"),
                Attribute(name = "Google")
            ),
            "certifications" to listOf(
                Certification(name = "ISO 9001", fileLocation = ""),
                Certification(name = "ISO 14023", fileLocation = "")
            )

        )

        val body = template(
            resource = "templates/products/seller/create.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/products/seller/create-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
    }

    @RequestMapping(
        "/products/{id}",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun showProduct(@PathVariable id: String): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val product: Product = productService.find(id)

        val address: String = product.user?.address ?: ""

        val data: Map<String, Any> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications,

            "id" to product.id!!,
            "name" to product.name,
            "description" to product.description,
            "minimum" to product.minimum,
            "maximum" to product.maximum,
            "batch" to product.batchSize,
            "images" to product.images,
            "address" to address
        )

        val body = template(
            resource = "templates/products/seller/show.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/products/seller/show-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
    }

    @RequestMapping(
        "/products/{id}/edit",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun editProduct(@PathVariable id: String): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val product: Product = productService.find(id)

        val attributes: List<Attribute> = product
            .productAttributes
            .mapNotNull { item ->
                item.attribute
            }

        val data: Map<String, Any> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications,

            "id" to product.id!!,
            "name" to product.name,
            "description" to product.description,
            "minimum" to product.minimum,
            "maximum" to product.maximum,
            "batch" to product.batchSize,
            "images" to product.images,
            "attributes" to attributes,
            "tags" to listOf(
                Attribute(name = "Apple"),
                Attribute(name = "Microsoft"),
                Attribute(name = "Google")
            ),
            "certifications" to listOf(
                Certification(name = "ISO 9001", fileLocation = ""),
                Certification(name = "ISO 14023", fileLocation = "")
            )
        )

        val body = template(
            resource = "templates/products/seller/edit.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/products/seller/edit-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
    }

    /**
     * Pricing Routes
     */
    @RequestMapping(
        "/pricings",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun pricings(): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val data: Map<String, Any?> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications
        )

        val body = template(
            resource = "templates/pricings/seller/index.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/pricings/favorites",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun favoritePricings(): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val data: Map<String, Any?> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications
        )

        val body = template(
            resource = "templates/pricings/favorites.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/pricings/history",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun pricingsHistory(): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val data: Map<String, Any?> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications
        )

        val body = template(
            resource = "templates/pricings/history.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/pricings/{id}",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun showPricing(@PathVariable id: String): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val data: Map<String, Any?> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications
        )

        val body = template(
            resource = "templates/pricings/show.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    /**
     * User Routes
     */
    @RequestMapping(
        "/profile/{id}",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun showProfile(@PathVariable id: String): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val data: Map<String, Any?> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications
        )

        val body = template(
            resource = "templates/user/seller/show.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/profile/{id}/edit",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun createProfile(@PathVariable id: String): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val data: Map<String, Any?> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications
        )

        val body = template(
            resource = "templates/user/seller/edit.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    /**
     * Messages Routes
     */
    @RequestMapping(
        "/messages",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun messages(): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val data: Map<String, Any?> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications
        )

        val body = template(
            resource = "templates/messages/index.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/chatbox",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun chatbox(): ModelAndView {
        val newAttributes: List<Attributes> = listOf()
        val newTags: List<Attributes> = listOf()
        val newCertifications: List<Certification> = listOf()

        val data: Map<String, Any?> = mapOf(
            "newTags" to newTags,
            "newAttributes" to newAttributes,
            "newCertifications" to newCertifications
        )

        val body = template("templates/messages/chatbox.mustache")

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    /**
     * Util Routes
     */
    @RequestMapping(
        "/verify",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun verify(): ModelAndView {
        return ModelAndView("verify")
    }
    @RequestMapping(
        "/modals",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun modals(): ModelAndView {
        return ModelAndView("modals")
    }

    private fun header(): String = TemplateBuilder(
        templateName = "templates/common/header/seller/logged.mustache",
        factory = factory)
        .build()

    private fun template(resource: String): String {
        return TemplateBuilder(templateName = resource, factory = factory)
            .build()
    }

    private fun template(
        resource: String,
        data: Map<String, Any?>
    ): String {
        return TemplateBuilder(
            templateName = resource,
            factory = factory)
            .data(data)
            .build()
    }

}