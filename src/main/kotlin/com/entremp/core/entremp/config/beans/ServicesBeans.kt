package com.entremp.core.entremp.config.beans

import com.entremp.core.entremp.service.BudgetService
import com.entremp.core.entremp.service.PricingService
import com.entremp.core.entremp.service.ProductService
import com.entremp.core.entremp.service.UserService
import org.springframework.context.support.BeanDefinitionDsl

object ServicesBeans {

    fun beans(): BeanDefinitionDsl = org.springframework.context.support.beans {
        bean {
            UserService(
                    usersRepository = ref(),
                    certificationRepository = ref(),
                    fileStorageService = ref()
            )
        }

        bean {
            ProductService(
                    productsRepository = ref(),
                    categoriesRepository = ref(),
                    productCategoriesRepository = ref(),
                    productAttributeRepository = ref(),
                    productImageRepository = ref(),
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
    }

}