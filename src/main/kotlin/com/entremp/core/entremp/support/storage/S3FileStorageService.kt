package com.entremp.core.entremp.support.storage

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.net.URL

@Service
class S3FileStorageService {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private final val credentials: AWSCredentials = BasicAWSCredentials(
        "AKIAZI725OW4K267XC7M",
        "7F6hOcK7rZwchiBDulEJs6/l8Cm5pAFCVjTW7iXv"
    )

    private final val client : AmazonS3 = AmazonS3ClientBuilder
        .standard()
        .withCredentials(
            AWSStaticCredentialsProvider(credentials)
        )
        .withRegion(Regions.SA_EAST_1)
        .build()

    private final val bucketName = "entremp"

    private final val internalStorageService = FileStorageService()

    fun store(file: MultipartFile, fileName: String?): URL {
        createBucketIfNotExist()

        internalStorageService.store(file, fileName)
        val internalFile: File = internalStorageService.read(fileName)

        val request = PutObjectRequest(
            bucketName,
            fileName,
            internalFile
        )

        request.cannedAcl = CannedAccessControlList.PublicRead

        client.putObject(request)

        return internalFile.toURI().toURL()
    }

    fun read(fileName: String) {
        val s3Object = client.getObject(
            bucketName,
            fileName
        )

        s3Object.objectContent
    }


    private fun createBucketIfNotExist(){
        if(client.doesBucketExistV2(bucketName)){
            logger.info("Bucket already exists")
        } else {
            logger.info("Creating S3 bucket $bucketName")
            client.createBucket(bucketName)
        }
    }
}