package com.entremp.core.entremp.support.storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:application.properties")
class AwsConfig{
    @Value("\${aws.credentials.access}")
    lateinit var accessKey: String

    @Value("\${aws.credentials.secret}")
    lateinit var secretKey: String

    @Value("\${aws.s3.bucket.name}")
    lateinit var bucketName: String
}