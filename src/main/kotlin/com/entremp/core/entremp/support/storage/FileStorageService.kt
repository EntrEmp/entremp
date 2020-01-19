package com.entremp.core.entremp.support.storage

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.net.URL

@Service
class FileStorageService {

    private final val storagePath: String = "/resources/"

    fun store(file: MultipartFile, fileName: String?): URL {
        val destination = getFile(fileName)

        file.transferTo(destination)

        return destination.toURI().toURL()
    }

    fun read(fileName: String?): File {
        return getFile(fileName)
    }

    private fun getFile(fileName: String?): File {
        val classLoaderPath: String = javaClass.classLoader.getResource(".").path.removeSuffix("/classes/")
        return File("$classLoaderPath$storagePath$fileName")
    }
}