package com.entremp.core.entremp.support.storage

import org.apache.commons.io.FileUtils
import org.junit.Test
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*


class FileStorageServiceTest {
    private val imageDirectory: Path = Paths.get("src", "test", "resources", "images")
    private val pdfDirectory: Path = Paths.get("src", "test", "resources", "files")

    private val fileStorageService = FileStorageService(
        Paths.get("src", "test", "resources")
    )

    @Test
    fun testStorageJPG(){
        testFileStorage(
            originalFileName = "user.jpg",
            filename = UUID.randomUUID().toString(),
            extension = "jpg"
        )
    }

    @Test
    fun testStoragePNG(){
        testFileStorage(
            originalFileName = "user.png",
            filename = UUID.randomUUID().toString(),
            extension = "png"
        )
    }

    @Test
    fun testStorageJPEG(){
        testFileStorage(
            originalFileName = "user.jpeg",
            filename = UUID.randomUUID().toString(),
            extension = "jpeg"
        )
    }

    @Test
    fun testStoragePDF(){
        val imageDirectoryPath: String = pdfDirectory.toFile().getAbsolutePath()
        val file = File("$imageDirectoryPath/test.pdf")

        val multipartFile: MultipartFile =  MockMultipartFile(
            "test.pdf",
            file.readBytes()
        )


        val result: File = fileStorageService.store(
            file = multipartFile,
            filename = UUID.randomUUID().toString(),
            defaultExtension = "pdf"
        )

        val sameContent: Boolean = FileUtils.contentEquals(file, result)
        val deleted: Boolean = result.delete()

        assert(sameContent)
        assert(deleted)
    }


    private fun testFileStorage(originalFileName: String,
                        filename: String,
                        extension: String){
        val imageDirectoryPath: String = imageDirectory.toFile().getAbsolutePath()
        val file = File("$imageDirectoryPath/$originalFileName")

        val multipartFile: MultipartFile =  MockMultipartFile(
            originalFileName,
            file.readBytes()
        )


        val result: File = fileStorageService.store(
            file = multipartFile,
            filename = filename,
            defaultExtension = extension
        )

        val sameContent: Boolean = FileUtils.contentEquals(file, result)
        val deleted: Boolean = result.delete()

        assert(sameContent)
        assert(deleted)
    }
}