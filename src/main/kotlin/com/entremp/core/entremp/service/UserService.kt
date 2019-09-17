package com.entremp.core.entremp.service

import com.entremp.core.entremp.data.user.CertificationRepository
import com.entremp.core.entremp.data.user.UsersRepository
import com.entremp.core.entremp.model.certification.Certification
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.support.FileStorageService
import com.entremp.core.entremp.support.JavaSupport.extension

import com.entremp.core.entremp.support.JavaSupport.unwrap
import org.springframework.web.multipart.MultipartFile
import java.net.URL
import java.util.*

class UserService(
        private val usersRepository: UsersRepository,
        private val certificationRepository: CertificationRepository,
        private val fileStorageService: FileStorageService
) {

    fun getAll(): Iterable<User>{
        return usersRepository.findAll()
    }

    fun register(email: String, password: String): User {
        return usersRepository.save(
                User(
                        email = email,
                        passwd = password,
                        token = UUID.randomUUID().toString()
                )
        )
    }

    fun updateUserData(
            id: String,
            password: String,
            cuit: Long,
            address: String,
            phone: Long
    ): User {
        val exists: User? = usersRepository.findById(id).unwrap()
        if(exists != null){
            val user: User = exists.copy(
                    passwd = password,
                    cuit = cuit,
                    address = address,
                    phone = phone
            )

            return usersRepository.save(user)
        } else {
            throw RuntimeException("User not found for id $id")
        }
    }

    fun remove(id: String) {
        return usersRepository.deleteById(id)
    }

    fun find(id: String): User {
        val user: User? = usersRepository.findById(id).unwrap()
        if(user != null) {
            return user
        } else {
            throw RuntimeException("User not found for id $id")
        }
    }

    fun addCertification(
            id: String,
            name: String,
            file: MultipartFile
    ): Certification {
        val user: User? = usersRepository.findById(id).unwrap()
        if(user != null) {
            val certification = certificationRepository.save(
                    Certification(
                            user = user,
                            name = name,
                            fileLocation = ""
                    )
            )

            val extension : String? = file.extension()
            val fileName = "${certification.id}.$extension"

            val url: URL = fileStorageService.store(file, fileName)

            return certificationRepository.save(
                    certification.copy(
                            fileLocation = url.toString()
                    )
            )

        } else {
            throw RuntimeException("User not found for id $id")
        }
    }

    fun removeCertification(certificationId: String){
        // TODO remove image from file storage server
        certificationRepository.deleteById(certificationId)
    }

}