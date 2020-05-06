package com.entremp.core.entremp.support

import org.springframework.web.multipart.MultipartFile
import java.util.*

object JavaSupport {
    fun <T> Optional<T>.unwrap(): T? =
        orElse(null)

    fun MultipartFile.extension(): String? =
        if(originalFilename.isNullOrEmpty()){
            null
        } else {
            originalFilename
                ?.substringAfterLast('.')
        }

}