package com.entremp.core.entremp.support.storage

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.multipart.MultipartFile
import java.io.File

class S3FileStorageService(
    private val config: AwsConfig,
    private val localStorage: FileStorageService
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private val client : AmazonS3 = AmazonS3ClientBuilder
        .standard()
        .withCredentials(
            AWSStaticCredentialsProvider(
                BasicAWSCredentials(
                    config.accessKey,
                    config.secretKey
                )
            )
        )
        .withRegion(Regions.SA_EAST_1)
        .build()

    fun store(file: MultipartFile,
              filename: String,
              defaultExtension: String): File {
        createBucketIfNotExist()

        val internal: File = localStorage.store(
            file,
            filename,
            defaultExtension
        )

        val request = PutObjectRequest(
            config.bucketName,
            internal.name,
            internal
        )

        request.cannedAcl = CannedAccessControlList.PublicRead

        client.putObject(request)

        if(internal.delete()){
            logger.info("deleted internal file $internal")
        }

        return internal
    }

    fun remove(filename: String): Unit {
        try {
            val request = DeleteObjectRequest(
                config.bucketName,
                filename
            )

            client.deleteObject(request)
        } catch(exception: Throwable){
            logger.error("Could not delete file from AWS S3 for file $filename", exception)
        }
    }

    private fun createBucketIfNotExist(){
        if(client.doesBucketExistV2(config.bucketName)){
            logger.info("Bucket already exists")
        } else {
            logger.info("Creating S3 bucket ${config.bucketName}")
            client.createBucket(config.bucketName)
        }
    }
}