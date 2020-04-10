package com.entremp.core.entremp.data.user

import com.entremp.core.entremp.model.user.UserAddress
import com.entremp.core.entremp.model.user.UserImage
import org.springframework.data.repository.CrudRepository

interface UserAddressRepository: CrudRepository<UserAddress, String>{}