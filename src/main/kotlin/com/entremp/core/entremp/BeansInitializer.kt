package com.entremp.core.entremp

import com.entremp.core.entremp.config.beans.ConfigBeans
import com.entremp.core.entremp.config.beans.DataSourceBeans
import com.entremp.core.entremp.config.beans.ServicesBeans
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

class BeansInitializer: ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(applicationContext: GenericApplicationContext) {
        ConfigBeans.beans().initialize(applicationContext)
        DataSourceBeans.beans().initialize(applicationContext)
        ServicesBeans.beans().initialize(applicationContext)
    }
}