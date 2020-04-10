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
import java.io.FileOutputStream
import java.net.URL

class S3FileStorageService(
    val config: AwsConfig
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

    fun store(file: MultipartFile, fileName: String?): URL {
        createBucketIfNotExist()


        val internalFile: File = transferToFile(file, fileName!!)

        val request = PutObjectRequest(
            config.bucketName,
            fileName,
            internalFile
        )

        request.cannedAcl = CannedAccessControlList.PublicRead

        client.putObject(request)

        if(internalFile.delete()){
            logger.info("deleted internal file $fileName")
        }

        return internalFile.toURI().toURL()
    }

    fun remove(fileName: String) {
        val request = DeleteObjectRequest(
            config.bucketName,
            fileName
        )

        client.deleteObject(request)
    }

    private fun createBucketIfNotExist(){
        if(client.doesBucketExistV2(config.bucketName)){
            logger.info("Bucket already exists")
        } else {
            logger.info("Creating S3 bucket ${config.bucketName}")
            client.createBucket(config.bucketName)
        }
    }

    private fun transferToFile(multipart: MultipartFile, fileName: String): File {
        val converted = File("media/$fileName")
        val fos = FileOutputStream(converted)
        fos.write(multipart.bytes)
        fos.close()
        return converted
    }
}