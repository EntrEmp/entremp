package com.entremp.core.entremp.web

import com.entremp.core.entremp.api.PageStatus
import com.entremp.core.entremp.api.SearchPage
import com.entremp.core.entremp.api.chat.MessagePreviewDTO
import com.entremp.core.entremp.api.chat.ShowMessageDTO
import com.entremp.core.entremp.api.product.ProductFilterDTO
import com.entremp.core.entremp.api.product.ShowFilterDTO
import com.entremp.core.entremp.controllers.Authenticated
import com.entremp.core.entremp.data.AttributesRepository
import com.entremp.core.entremp.data.CategoriesRepository
import com.entremp.core.entremp.data.CertificationTagsRepository
import com.entremp.core.entremp.data.TagsRepository
import com.entremp.core.entremp.model.*
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.product.FavoriteProduct
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.service.ChatService
import com.entremp.core.entremp.service.PricingService
import com.entremp.core.entremp.service.ProductService
import com.entremp.core.entremp.service.UserService
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
@RequestMapping("/web/buyer")
class BuyerRenderController(
    private val userService: UserService,
    private val productService: ProductService,
    private val pricingService: PricingService,
    private val categoriesRepository: CategoriesRepository,
    private val attributesRepository: AttributesRepository,
    private val certificationTagsRepository: CertificationTagsRepository,
    private val tagsRepository: TagsRepository,
    private val chatService: ChatService
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
        val body = template("templates/common/home.mustache")

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", "")
            .addObject("tags", tags)
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
        val searchFilter: ProductFilterDTO? = productSearchFilter(request)

        val products: Page<Product> = productService.filter(searchFilter)
        val favorites: List<FavoriteProduct> = productService.getFavorites(getAuthUser()!!)

        val pages: List<SearchPage> =
            (1..products.totalPages)
                .toList()
                .map { value ->
                    SearchPage(
                        value = value
                    )
                }

        val pageStatus: PageStatus? =
            if(products.isEmpty){
                null
            } else if(searchFilter != null && searchFilter.searchPage > 1){
                if(searchFilter.searchPage.equals(products.totalPages)){
                    PageStatus.inactiveLast()
                } else {
                    PageStatus.bothActive()
                }
            } else {
                PageStatus.inactiveFirst()
            }

        val showFilter: ShowFilterDTO = showableFilter(searchFilter)

        val data: Map<String, Any?> = mapOf(
            "products" to products.content,
            "categories" to categories,
            "attributes" to attributes,
            "tags" to tags,
            "certifications" to certifications,
            "favorites" to favorites,
            "showFilter" to showFilter,
            "searchFilter" to searchFilter,
            "pages" to pages,
            "pageStatus" to pageStatus
        )

        val body = template(
            resource = "templates/products/search.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/products/search-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header(searchFilter?.criteria))
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
            .addObject("showFilter", showFilter)
    }

    @RequestMapping(
        "/products/{id}",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun showProduct(@PathVariable id: String): ModelAndView {
        val product: Product = productService.find(id)

        val data: Map<String, Any?> = mapOf(
            "images" to product.images,
            "name" to product.name,
            "description" to product.description,
            "minimum" to product.minimum,
            "maximum" to product.maximum,
            "batch" to product.batchSize,
            "mainImage" to product.mainImage(),
            "id" to id
        )

        val body = template(
            resource = "templates/products/buyer/show.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/products/buyer/show-footer.mustache",
            data = data
        )


        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
    }

    @RequestMapping(
        "/products/favorites",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun favoriteProducts(): ModelAndView {
        val favorites: List<Product> = productService
            .getFavorites(getAuthUser()!!)
            .mapNotNull { favorite ->
                favorite.product
            }

        val data: Map<String, Any?> = mapOf(
            "attributes" to attributes,
            "categories" to categories,
            "tags" to tags,
            "certifications" to certifications,
            "products" to favorites,
            "favorites" to favorites
        )

        val body = template(
            resource = "templates/products/favorites.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/products/favorites-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
            .addObject("certifications", certifications)
            .addObject("attributes", attributes)

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
            .getActiveByBuyer(
                buyer = getAuthUser() !!
            )
            .toList()

        val data: Map<String, Any?> = mapOf(
            "attributes" to attributes,
            "categories" to categories,
            "tags" to tags,
            "certifications" to certifications,
            "pricings" to pricings
        )

        val body = template(
            resource = "templates/pricings/buyer/index.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/pricings/buyer/index-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
    }

    @RequestMapping(
        "/pricings/history",
        method = [RequestMethod.GET],
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun pricingsHistory(): ModelAndView {
        val pricings: List<Pricing> = pricingService
            .getHistoryByBuyer(
                buyer = getAuthUser() !!
            )
            .toList()

        val data: Map<String, Any?> = mapOf(
            "attributes" to attributes,
            "categories" to categories,
            "tags" to tags,
            "certifications" to certifications,
            "pricings" to pricings
        )

        val body = template(
            resource = "templates/pricings/buyer/history.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/pricings/buyer/history-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
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
            "attributes" to attributes,
            "categories" to categories,
            "tags" to tags,
            "certifications" to certifications,
            "pricing" to pricing,
            "deliveryTerm" to deliveryTerm,
            "sample" to sample
        )

        val body = template(
            resource = "templates/pricings/buyer/show.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/pricings/buyer/show-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
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

        val products: List<Product> = emptyList()

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
            resource = "templates/user/buyer/show.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/user/buyer/show-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
            .addObject("products", products)
            .addObject("hasProducts", products.isNotEmpty())
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

        val body = template(
            resource = "templates/user/buyer/edit.mustache",
            data = data
        )

        val footer = template(
            resource = "templates/user/buyer/show-footer.mustache",
            data = data
        )

        return ModelAndView("common/general")
            .addObject("header", header())
            .addObject("body", body)
            .addObject("footer", footer)
            .addObject("tags", tags)
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

        val chats: List<MessagePreviewDTO> = chatService.findChats(
            user = auth!!,
            role = "buyer"
        ).map { chat ->
            val preview: String = chat
                .getLastMessage()
                .let { message ->
                    message
                        ?.message
                        ?.slice(IntRange(0, 97))
                        ?: ""
                }

            MessagePreviewDTO(
                name = chat.provider!!.name,
                date = chat
                    .getLastMessage()
                    .let { message ->
                        message
                            ?.sent
                            ?: DateTime.now(DateTimeZone.UTC)
                    }
                    .toString("dd/MM/yyyy"),
                message = "$preview...",
                chatId = chat.id!!,
                role = "buyer"
            )
        }

        val data: Map<String,Any> = mapOf(
            "role" to "buyer",
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
        val role: String = "buyer"

        val chats: List<MessagePreviewDTO> = chatService.findChats(
            user = auth!!,
            role = role
        ).map { chat ->
            val preview: String = chat
                .getLastMessage()
                .let { message ->
                    message
                        ?.message
                        ?: ""
                }

            MessagePreviewDTO(
                name = chat.provider!!.name,
                date = chat
                    .getLastMessage()
                    .let { message ->
                        message
                            ?.sent
                            ?: DateTime.now(DateTimeZone.UTC)
                    }
                    .toString("dd/MM/yyyy"),
                message =
                if(preview.isEmpty()){
                    preview
                } else {
                    "$preview..."
                },
                chatId = chat.id!!,
                role = role
            )
        }

        val messages: List<ShowMessageDTO> = chatService
            .getMessages(id)
            .map { message ->
                ShowMessageDTO(
                    message = message.message,
                    styleClass = if(role == message.role){
                        "chat-question"
                    } else {
                        "chat-answers"
                    }
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

    private fun header(searchCriteria: String? = null): String = TemplateBuilder(
        templateName = "templates/common/header/buyer/logged.mustache",
        factory = factory)
        .data(
            mapOf(
                "loggedId" to getAuthUser()?.id,
                "searchCriteria" to searchCriteria
            )
        )
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

    private fun showableFilter(filter: ProductFilterDTO?): ShowFilterDTO =
        ShowFilterDTO(
            categories = filter
                ?.categories
                ?.mapNotNull { id ->
                    categories
                        .find { category ->
                            category.id == id
                        }
                }
                ?: emptyList(),
            attributes = filter
                ?.attributes
                ?.mapNotNull { id ->
                    attributes
                        .find { attribute ->
                            attribute.id == id
                        }
                }
                ?: emptyList(),
            certifications = filter
                ?.certifications
                ?.mapNotNull { id ->
                    certifications
                        .find { certification ->
                            certification.id == id
                        }
                }
                ?: emptyList(),
            criteria = filter?.criteria ?: ""
        )


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