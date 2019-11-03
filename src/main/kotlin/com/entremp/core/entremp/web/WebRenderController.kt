package com.entremp.core.entremp.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
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
        "/login",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun login(): ModelAndView {
        return ModelAndView("login")
    }

    @RequestMapping(
        "/recover",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun recover(): ModelAndView {
        return ModelAndView("recover")
    }

    @RequestMapping(
        "/verify",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun verify(): ModelAndView {
        return ModelAndView("verify")
    }

    @RequestMapping(
        "/account",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun createAccount(): ModelAndView {
        return ModelAndView("account")
    }

    @RequestMapping(
        "/account-select",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun createAccountSelect(): ModelAndView {
        return ModelAndView("accountSelect")
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
        "/modals",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun modals(): ModelAndView {
        return ModelAndView("modals")
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
        return ModelAndView("search")
    }

    @RequestMapping(
        "/profile/{id}",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun showProfile(@PathVariable id: String): ModelAndView {
        return ModelAndView("profile")
    }

    @RequestMapping(
        "/profile",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun createProfile(): ModelAndView {
        return ModelAndView("profileForm")
    }

    @RequestMapping(
        "/product/{id}",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun showProduct(@PathVariable id: String): ModelAndView {
        return ModelAndView("product")
    }

    @RequestMapping(
        "/product",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun createProduct(): ModelAndView {
        return ModelAndView("productForm")
    }

    @RequestMapping(
        "/pricings",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun pricings(): ModelAndView {
        return ModelAndView("pricings")
    }

    @RequestMapping(
        "/chatbot",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun chatbot(): ModelAndView {
        return ModelAndView("chatbot")
    }

}