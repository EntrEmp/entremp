package com.entremp.core.entremp.support.storage

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream

import com.entremp.core.entremp.support.JavaSupport.extension
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

data class FileStorageService(private val storagePath : Path) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun store(file: MultipartFile,
              filename: String,
              defaultExtension: String): File {
        val directory: File = storagePath.toFile()

        val extension: String = file.extension()
            ?: defaultExtension

        val name = "$filename.$extension"

        val destination = File(
            directory,
            name
        )

        val fos = FileOutputStream(destination)
        fos.write(file.bytes)
        fos.close()

        return destination
    }
}