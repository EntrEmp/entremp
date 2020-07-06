package com.entremp.core.entremp.web

import com.entremp.core.entremp.api.chat.MessagePreviewDTO
import com.entremp.core.entremp.api.chat.ShowMessageDTO
import com.entremp.core.entremp.api.product.ProductFilterDTO
import com.entremp.core.entremp.controllers.Authenticated
import com.entremp.core.entremp.data.AttributesRepository
import com.entremp.core.entremp.data.CategoriesRepository
import com.entremp.core.entremp.data.CertificationTagsRepository
import com.entremp.core.entremp.data.TagsRepository
import com.entremp.core.entremp.model.*
import com.entremp.core.entremp.model.notification.Notification
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.service.*
import com.entremp.core.entremp.support.ObjectMapperFactory
import com.entremp.core.entremp.support.templates.TemplateBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.apache.commons.codec.binary.Base64
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/seller")
class SellerRenderController(
    private val userService: UserService,
    private val notificationService: NotificationService,
    private val productService: ProductService,
    private val pricingService: PricingService,
    private val tagsRepository: TagsRepository,
    private val certificationTagsRepository: CertificationTagsRepository,
    private val chatService: ChatService,
    private val categoriesRepository: CategoriesRepository,
    private val attributesRepository: AttributesRepository
): Authenticated {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private val factory: MustacheFactory = DefaultMustacheFactory()

    private val mapper: ObjectMapper = ObjectMapperFactory.camelCaseMapper

    private val categories: List<Category> = categoriesRepository
        .findAll()
        .toList()

    private val attributes: List<Attribute> = attributesRepository
        .findAll()
        .toList()

    val certifications: List<CertificationTag> = certificationTagsRepository
        .findAll()
        .toList()

    val tags: List<Tag> = tagsRepository
        .findAll()
        .toList()


    /**
     * Home Routes
     */
    @RequestMapping(
        "/home",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun home(): ModelAndView {
        val image = "verde.png"

        val products = productService.productsForTag("Covid 19").content

        val data: Map<String, Any?> = mapOf(
            "image" to image,
            "products" to products
        )

        val body = template(
            resource = "templates/common/home.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("attributes", attributes)
    }

    /**
     * Notification Routes
     */
    @RequestMapping(
        "/notifications",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun notifications(request: HttpServletRequest): ModelAndView {
        val authenticated: User? = getAuthUser()

        val notifications: List<Notification> = notificationService
            .findByUser(authenticated!!)
            .toList()
            .filterNot { notification ->
                notification.seen
            }

        val data: Map<String, Any?> = mapOf(
            "notifications" to notifications,
            "role" to "seller"
        )

        val body = template(
            resource = "templates/notifications/seller/index.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/notifications/seller/index-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("attributes", attributes)
            .addObject("categories", categories)

    }

    /**
     * Product Routes
     */
    @RequestMapping(
        "/products",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun products(request: HttpServletRequest): ModelAndView {
        val authenticated: User? = getAuthUser()

        val filter: ProductFilterDTO? = productSearchFilter(request)

        val products: List<Product> = productService.findByUser(authenticated!!)

        val data: Map<String, Any?> = mapOf(
            "products" to products
        )

        val body = template(
            resource = "templates/products/index.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header(filter?.criteria))
            .addObject("body", body)
            .addObject("footer", "")
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("attributes", attributes)
            .addObject("categories", categories)

    }

    @RequestMapping(
        "/products/create",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun createProduct(): ModelAndView {
        val data: Map<String, Any?> = mapOf(
            "categories" to categories,
            "attributes" to attributes,
            "tags" to tags,
            "certifications" to certifications
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
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("attributes", attributes)
            .addObject("categories", categories)

    }

    @RequestMapping(
        "/products/{id}",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun showProduct(@PathVariable id: String): ModelAndView {
        val product: Product = productService.find(id)

        val address: String = product.user?.address ?: ""

        val data: Map<String, Any> = mapOf(
            "id" to product.id!!,
            "name" to product.name,
            "description" to product.description,
            "minimum" to product.minimum,
            "maximum" to product.maximum,
            "batch" to product.batchSize,
            "images" to product.images,
            "product" to product,
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
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("attributes", attributes)
            .addObject("categories", categories)

    }

    @RequestMapping(
        "/products/{id}/edit",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun editProduct(@PathVariable id: String): ModelAndView {
        val product: Product = productService.find(id)

        val data: Map<String, Any> = mapOf(
            "id" to product.id!!,
            "name" to product.name,
            "description" to product.description,
            "minimum" to product.minimum,
            "maximum" to product.maximum,
            "batch" to product.batchSize,
            "images" to product.images,
            "productCategories" to product
                .productCategories
                .mapNotNull { item ->
                    item.category
                },

            "productAttributes" to product
                .productAttributes
                .mapNotNull { item ->
                    item.attribute
                },
            "activeAttributes" to product
                .activeAttributes()
                .mapNotNull { item ->
                    item.attribute
                },

            "productTags" to product
                .productTags
                .mapNotNull { item ->
                    item.tag
                },
            "productCertifications" to product
                .productCertifications
                .mapNotNull { item ->
                    item.certification
                },
            "categories" to categories,
            "attributes" to attributes,
            "tags" to tags,
            "certifications" to certifications
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
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("attributes", attributes)
            .addObject("categories", categories)

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
        val pricings: List<Pricing> = pricingService
            .getActiveByProvider(
                provider = getAuthUser() !!
            )
            .toList()

        val data: Map<String, Any?> = mapOf(
            "categories" to categories,
            "attributes" to attributes,
            "tags" to tags,
            "certifications" to certifications,
            "pricings" to pricings
        )

        val body = template(
            resource = "templates/pricings/seller/index.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/pricings/seller/index-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("attributes", attributes)
            .addObject("categories", categories)


    }

    @RequestMapping(
        "/pricings/history",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun pricingsHistory(): ModelAndView {
        val pricings: List<Pricing> = pricingService
            .getHistoryByProvider(
                provider = getAuthUser() !!
            )
            .toList()

        val data: Map<String, Any?> = mapOf(
            "categories" to categories,
            "attributes" to attributes,
            "tags" to tags,
            "certifications" to certifications,
            "pricings" to pricings
        )

        val body = template(
            resource = "templates/pricings/seller/history.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/pricings/seller/history-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("categories", categories)
            .addObject("attributes", attributes)
    }

    @RequestMapping(
        "/pricings/{id}",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun showPricing(@PathVariable id: String): ModelAndView {
        val pricing: Pricing = pricingService.find(id)

        val deliveryTerm: String = when(pricing.deliveryTerm){
            DeliveryTerm.IN_15_DAYS ->
                "15 dias"
            DeliveryTerm.IN_30_DAYS ->
                "30 dias"
            DeliveryTerm.IN_45_DAYS ->
                "45 dias"
        }

        val sample: String =
            if(pricing.sample){
                "SI"
            } else {
                "NO"
            }

        val data: Map<String, Any?> = mapOf(
            "categories" to categories,
            "attributes" to attributes,
            "tags" to tags,
            "certifications" to certifications,
            "pricing" to pricing,
            "deliveryTerm" to deliveryTerm,
            "sample" to sample
        )


        val body = template(
            resource = "templates/pricings/seller/show.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/pricings/seller/show-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("categories", categories)
            .addObject("attributes", attributes)

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
        val user: User? = userService.find(id)
        val image: String = user?.images
            ?.firstOrNull()
            ?.s3Link()
            ?: "/images/profile/user.png"

        val data: Map<String, Any?> = mapOf(
            "id" to id,
            "name" to user?.name,
            "mail" to user?.email,
            "phone" to user?.phone,
            "cuit" to user?.cuit,
            "image" to image,
            "addresses" to user?.addresses
        )

        val body = template(
            resource = "templates/user/seller/show.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/user/seller/show-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("categories", categories)
            .addObject("attributes", attributes)

    }

    @RequestMapping(
        "/profile/{id}/edit",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun createProfile(@PathVariable id: String): ModelAndView {
        val user: User? = userService.find(id)
        val image: String = user?.images
            ?.firstOrNull()
            ?.s3Link()
            ?: "/images/profile/rock.png"

        val data: Map<String, Any?> = mapOf(
            "userId" to id,
            "name" to user?.name,
            "mail" to user?.email,
            "phone" to user?.phone,
            "cuit" to user?.cuit,
            "image" to image,
            "addresses" to user?.addresses
        )

        val footer = template(
            resource = "templates/user/seller/edit-footer.mustache",
            data = data
        )

        val body = template(
            resource = "templates/user/seller/edit.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("categories", categories)
            .addObject("attributes", attributes)

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
        val auth: User? = getAuthUser()
        val role = "seller"

        val chats: List<MessagePreviewDTO> = chatService.findChats(
            user = auth!!,
            role = role
        ).map { chat ->

            val buyer: String = chat
                .buyer!!
                .name

            MessagePreviewDTO(
                name = buyer,
                date = chat
                    .getLastMessage()
                    .let { message ->
                        message
                            ?.sent
                            ?: DateTime.now(DateTimeZone.UTC)
                    }
                    .toString("dd/MM/yyyy"),
                message = chat
                    .getLastMessage()
                    ?.message
                    ?: "$buyer ha aceptado tu cotización.",
                chatId = chat.id!!,
                role = role
            )
        }

        val data: Map<String,Any> = mapOf(
            "role" to role,
            "chats" to chats
        )

        val body = template(
            resource = "templates/messages/index.mustache",
            data = data)

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
            .addObject("tags", tags)
    }

    /**
     * Messages Routes
     */
    @RequestMapping(
        "/messages/{id}",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun showChat(@PathVariable id: String): ModelAndView {
        val auth: User? = getAuthUser()
        val role = "seller"

        val chats: List<MessagePreviewDTO> = chatService.findChats(
            user = auth!!,
            role = role
        ).map { chat ->

            val buyer: String = chat
                .buyer!!
                .name

            MessagePreviewDTO(
                name = buyer,
                date = chat
                    .getLastMessage()
                    .let { message ->
                        message
                            ?.sent
                            ?: DateTime.now(DateTimeZone.UTC)
                    }
                    .toString("dd/MM/yyyy"),
                message = chat
                    .getLastMessage()
                    ?.message
                    ?: "$buyer ha aceptado tu cotización.",
                chatId = chat.id!!,
                role = role
            )
        }

        val messages: List<ShowMessageDTO> = chatService
            .getMessages(id)
            .map { message ->
                ShowMessageDTO(
                    message = message.message,
                    styleClass =
                    if(message.role == role){
                        "chat-question"
                    } else {
                        "chat-answers"
                    },
                    avatar = message.avatar()
                )
            }

        val data: Map<String,Any> = mapOf(
            "role" to role,
            "chatId" to id,
            "chats" to chats,
            "messages" to messages
        )

        val body = template(
            resource = "templates/messages/show.mustache",
            data = data)

        val footer = template(
            resource = "templates/messages/show-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
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

    private fun header(searchCriteria: String? = null): String {
        val user: User? = getAuthUser()

        val notificationCount: Int? = notificationService
            .findByUser(user!!)
            .filterNot { notification ->
                notification.seen
            }
            .size

        val emptyNotifications: Boolean = notificationCount == 0

        return TemplateBuilder(
            templateName = "templates/common/header/seller/logged.mustache",
            factory = factory)
            .data(
                mapOf(
                    "loggedId" to user.id,
                    "searchCriteria" to searchCriteria,
                    "notifications" to if(emptyNotifications){
                        null
                    } else {
                        notificationCount
                    },
                    "emptyNotifications" to emptyNotifications
                )
            )
            .build()
    }

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

    private fun productSearchFilter(request: HttpServletRequest): ProductFilterDTO? =
        readCookie(request, "searchFilter")
            .let { cookie ->
                if(cookie == null){
                    null
                } else {
                    mapper.readValue(
                        String(
                            Base64.decodeBase64(
                                cookie.value
                            )
                        ),
                        ProductFilterDTO::class.java
                    )
                }
            }

    private fun readCookie(request: HttpServletRequest,
                           key: String): Cookie? =
        request.cookies
            .find { cookie ->
                cookie.name == key
            }

}