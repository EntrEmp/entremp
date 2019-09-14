package com.entremp.core.entremp.config.beans

import com.entremp.core.entremp.support.ObjectMapperFactory
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

object ConfigBeans {

    fun beans(): BeanDefinitionDsl = org.springframework.context.support.beans {
        bean {
            ObjectMapperFactory.snakeCaseMapper
        }
        bean<PasswordEncoder> {
            BCryptPasswordEncoder()
        }
    }
}