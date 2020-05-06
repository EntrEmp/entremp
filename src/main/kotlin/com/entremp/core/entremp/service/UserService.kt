package com.entremp.core.entremp.service

import com.entremp.core.entremp.api.user.EditUserDataDTO
import com.entremp.core.entremp.api.user.UserAddressDTO
import com.entremp.core.entremp.data.user.CertificationRepository
import com.entremp.core.entremp.data.user.UserAddressRepository
import com.entremp.core.entremp.data.user.UserImageRepository
import com.entremp.core.entremp.data.user.UsersRepository
import com.entremp.core.entremp.model.user.Certification
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.model.user.UserAddress
import com.entremp.core.entremp.model.user.UserImage
import com.entremp.core.entremp.support.JavaSupport.extension

import com.entremp.core.entremp.support.JavaSupport.unwrap
import com.entremp.core.entremp.support.storage.S3FileStorageService
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

class UserService(
        private val usersRepository: UsersRepository,
        private val userAddressRepository: UserAddressRepository,
        private val certificationRepository: CertificationRepository,
        private val userImageRepository: UserImageRepository,
        private val fileStorageService: S3FileStorageService
) {

    fun getAll(): Iterable<User>{
        return usersRepository.findAll()
    }

    fun register(email: String, password: String): User {
        val user: User = usersRepository.save(
            User(
                email = email,
                passwd = password,
                token = UUID.randomUUID().toString()
            )
        )

        return user

    }

    fun updateData(
        id: String,
        data: EditUserDataDTO
    ): User {
        val stored: User? = usersRepository.findById(id).unwrap()

        if(stored != null){
            val user: User = stored.copy(
                name = data.name,
                phone = data.phone,
                cuit = data.cuit
            )

            return usersRepository.save(user)
        } else {
            throw RuntimeException("User not found for id $id")
        }
    }

    fun updatePassword(
        id: String,
        encoded: String
    ): User {
        val stored: User? = usersRepository.findById(id).unwrap()

        if(stored != null){
            val user: User = stored.copy(
                passwd = encoded
            )

            return usersRepository.save(user)
        } else {
            throw RuntimeException("User not found for id $id")
        }
    }

    fun addAddress(
        id: String,
        data: UserAddressDTO
    ) {
        val stored: User? = usersRepository.findById(id).unwrap()

        if(stored != null){
            // TODO check values to be stored
            userAddressRepository.save(
                UserAddress(
                    user = stored,
                    address = "${data.street} ${data.number}",
                    state = data.state,
                    locality = data.town
                )
            )
        } else {
            throw RuntimeException("User not found for id $id")
        }
    }

    fun editAddress(
        id: String,
        addressId: String,
        data: UserAddressDTO
    ) {
        val stored: User? = usersRepository.findById(id).unwrap()
        val address: UserAddress? = userAddressRepository.findById(addressId).unwrap()

        if(stored != null && address != null){
            // TODO check values to be stored
            userAddressRepository.save(
                address.copy(
                    user = stored,
                    address = "${data.street} ${data.number}",
                    state = data.state,
                    locality = data.town
                )
            )
        } else {
            throw RuntimeException("User not found for id $id")
        }
    }

    fun removeAddress(
        id: String,
        userAddressId: String
    ) {
        val user: User? = usersRepository.findById(id).unwrap()
        val address: UserAddress? = userAddressRepository.findById(userAddressId).unwrap()

        if(user != null && address != null){
            userAddressRepository.deleteById(userAddressId)
        }

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

            userAddressRepository.save(
                UserAddress(
                    user = user,
                    address =  address
                )
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

            val extension : String = file.extension() ?: "pdf"
            val certificationFile: File = fileStorageService.store(
                file = file,
                filename = "${certification.id}",
                defaultExtension = extension
            )

            return certificationRepository.save(
                    certification.copy(
                        fileLocation = certificationFile
                            .toURI()
                            .toURL()
                            .toString()
                    )
            )

        } else {
            throw RuntimeException("User not found for id $id")
        }
    }

    fun removeCertification(certificationId: String){
        val certification: Certification? = certificationRepository
            .findById(certificationId)
            .unwrap()

        if(certification != null){
            certificationRepository.deleteById(certificationId)
            fileStorageService.remove(
                filename = certification.filename()
            )
        }
    }

    fun addImage(
        id: String,
        file: MultipartFile
    ) {
        val user: User? = usersRepository
            .findById(id)
            .unwrap()

        if(user != null) {
            user
                .images
                .forEach { image: UserImage ->
                    userImageRepository.deleteById(image.id!!)
                    if(image.fileLocation != null){
                        fileStorageService.remove(
                            filename = image.filename()
                        )
                    }
                }

            val storable = UserImage(
                user = user,
                fileLocation = ""
            )

            val image: UserImage = userImageRepository.save(storable)

            val extension : String = file.extension() ?: "jpg"
            val imageFile: File = fileStorageService.store(
                file = file,
                filename = "${image.id}",
                defaultExtension = extension
            )

            userImageRepository.save(
                image.copy(
                    fileLocation = imageFile
                        .toURI()
                        .toURL()
                        .toString()
                )
            )

        } else {
            throw RuntimeException("Product not found for id $id")
        }
    }
}