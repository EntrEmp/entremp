package com.entremp.core.entremp.web

import com.entremp.core.entremp.support.templates.TemplateBuilder
import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.support.RequestContextUtils
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/web")
class BasicRenderController {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private val factory: MustacheFactory = DefaultMustacheFactory()

    @RequestMapping(
        "/index",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun index(): ModelAndView {
        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", "")
            .addObject("footer", "")
    }

    @RequestMapping(
        "/login",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun login(request: HttpServletRequest): ModelAndView {
        val flashMap = RequestContextUtils.getInputFlashMap(request)
        val dataMap = mapOf(
            "flashSuccess" to flashMap?.get("success"),
            "flashError" to flashMap?.get("error")
        )

        val body = template("templates/auth/login.mustache", dataMap)

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/recover",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun recover(request: HttpServletRequest): ModelAndView {
        val flashMap = RequestContextUtils.getInputFlashMap(request)
        val dataMap = mapOf(
            "flashSuccess" to flashMap?.get("success"),
            "flashError" to flashMap?.get("error"),
            "mail" to flashMap?.get("mail")
        )
        val body = template("templates/auth/recover.mustache", dataMap)

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/reset",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun password(
        @RequestParam token: String,
        request: HttpServletRequest
    ): ModelAndView {
        val dataMap = mapOf(
            "token" to token
        )
        val body = template("templates/auth/password.mustache", dataMap)

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/choose",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun choose(): ModelAndView {
        val body = template("templates/auth/select.mustache")

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/sign-up",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun signUp(request: HttpServletRequest): ModelAndView {
        val flashMap = RequestContextUtils.getInputFlashMap(request)
        val dataMap = mapOf(
            "flashSuccess" to flashMap?.get("success"),
            "flashError" to flashMap?.get("error"),
            "prospect" to flashMap?.get("prospect")
        )

        val body = template("templates/auth/register.mustache", dataMap)
        val footer = template("templates/auth/register-footer.mustache")

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
    }

    @RequestMapping(
        "/verify",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun verify(request: HttpServletRequest): ModelAndView {
        val flashMap = RequestContextUtils.getInputFlashMap(request)
        val dataMap = mapOf(
            "flashSuccess" to flashMap?.get("success"),
            "flashError" to flashMap?.get("error")
        )

        val body = template("templates/auth/verify.mustache", dataMap)

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
    }

    @RequestMapping(
        "/faq",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun faq(): ModelAndView {
        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", "")
            .addObject("footer", "")
    }

    @RequestMapping(
        "/terms-and-conditions",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun termsAndConditions(): ModelAndView {
        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", "")
            .addObject("footer", "")
    }

    private fun header(): String =
        TemplateBuilder(
            templateName = "templates/common/header/simple.mustache",
            factory = factory
        )
        .build()

    private fun template(resource: String): String =
        TemplateBuilder(
            templateName = resource,
            factory = factory
            )
            .build()

    private fun template(resource: String, dataMap: Map<String, Any?>): String =
        TemplateBuilder(
            templateName = resource,
            factory = factory
            )
            .data(dataMap)
            .build()
}