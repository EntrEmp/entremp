package com.entremp.core.entremp.config.beans

import com.entremp.core.entremp.service.*
import com.entremp.core.entremp.support.storage.AwsConfig
import com.entremp.core.entremp.support.storage.FileStorageService
import com.entremp.core.entremp.support.storage.S3FileStorageService
import org.springframework.context.support.BeanDefinitionDsl
import java.nio.file.Path
import java.nio.file.Paths

object ServicesBeans {

    private val storagePath: Path = Paths.get("media")

    fun beans(): BeanDefinitionDsl = org.springframework.context.support.beans {
        bean {
            FileStorageService(
                storagePath = storagePath
            )
        }
        bean {
            val config: AwsConfig = ref()
            S3FileStorageService(
                config = config,
                localStorage = ref()
            )
        }
        bean {
            UserService(
                usersRepository = ref(),
                userAddressRepository = ref(),
                userImageRepository = ref(),
                certificationRepository = ref(),
                fileStorageService = ref()
            )
        }

        bean {
            ProductService(
                productsRepository = ref(),

                favoriteProductRepository = ref(),

                attributeRepository = ref(),
                categoriesRepository = ref(),
                productCategoriesRepository = ref(),

                productAttributeRepository = ref(),
                productImageRepository = ref(),

                tagsRepository = ref(),
                productTagRepository = ref(),

                certificationTagsRepository = ref(),
                productCertificationRepository = ref(),

                fileStorageService = ref()
            )
        }

        bean {
            PricingService(
                    pricingRepository = ref(),
                    sampleRepository = ref(),
                    pricingAttachementRepository = ref(),
                    fileStorageService = ref()
            )
        }

        bean {
            BudgetService(
                    pricingRepository = ref(),
                    budgetRepository = ref(),
                    budgetAttachementRepository = ref(),
                    fileStorageService = ref()
            )
        }

        bean {
            ChatService(
                chatRepository = ref()
            )
        }
    }

}