package com.entremp.core.entremp.web

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

@Controller
@RequestMapping("/web/buyer")
class BuyerRenderController {
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
        val body = template("templates/common/home.mustache")

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
        val body = template("templates/products/search.mustache")

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/products/{id}",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun showProduct(@PathVariable id: String): ModelAndView {
        val body = template(resource = "templates/products/buyer/show.mustache")


        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
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
        val body = template("templates/pricings/buyer/index.mustache")

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
        val body = template("templates/pricings/favorites.mustache")

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
        val body = template("templates/pricings/history.mustache")

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
        val body = template("templates/pricings/show.mustache")

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
        val body = template("templates/user/buyer/show.mustache")

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
        val body = template("templates/user/buyer/edit.mustache")

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
        val body = template("templates/messages/index.mustache")

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
        val body = template("templates/messages/chatbox.mustache")

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    private fun header(): String = TemplateBuilder(
        templateName = "templates/common/header/buyer/logged.mustache",
        factory = factory)
        .build()

    private fun template(resource: String): String {
        return TemplateBuilder(
            templateName = resource,
            factory = factory
        ).build()
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