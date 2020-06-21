package com.entremp.core.entremp.model

enum class DeliveryTerm {
    IN_15_DAYS,
    IN_30_DAYS,
    IN_45_DAYS;

    fun toHTMLString(): String {
        return when(this){
            IN_15_DAYS ->
                "15 días"
            IN_30_DAYS ->
                "30 días"
            IN_45_DAYS ->
                "45 días"
            else ->
                "DeliveryTerm unsettled"
        }
    }
}