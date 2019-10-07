package com.entremp.core.entremp.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/web")
class WebRenderController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @RequestMapping(
        "/example",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun example(): ModelAndView {
        return ModelAndView("example")
    }

    @RequestMapping(
        "/index",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun index(): ModelAndView {
        return ModelAndView("index")
    }

    @RequestMapping(
        "/messages",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun messages(): ModelAndView {
        return ModelAndView("messages")
    }

    @RequestMapping(
        "/products",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun products(): ModelAndView {
        return ModelAndView("products")
    }
}