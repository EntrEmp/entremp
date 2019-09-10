package com.entremp.core.entremp.model

abstract class Fileable(
        fileLocation: String?
){
    constructor(): this(fileLocation = "")
}