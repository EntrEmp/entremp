package com.entremp.core.entremp.support

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.net.URL

@Service
class FileStorageService {

    private final val storagePath: String = "/resources/"

    fun store(file: MultipartFile, fileName: String?): URL {
        val classLoaderPath: String = javaClass.classLoader.getResource(".").path.removeSuffix("/classes/")
        val destination = File("$classLoaderPath$storagePath$fileName")

        file.transferTo(destination)

        return destination.toURI().toURL()
    }
}