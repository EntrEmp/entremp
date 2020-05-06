package com.entremp.core.entremp.support

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class EntrEmpContext {
    @Value("\${entremp.server.address}")
    lateinit var address: String

    @Value("\${entremp.server.protocol}")
    lateinit var protocol: String

    @Value("\${entremp.server.port}")
    lateinit var port: String


    fun toServerAddress(): String =
        if(port.isEmpty()){
            "$protocol://$address"
        } else {
            "$protocol://$address:$port"
        }
}