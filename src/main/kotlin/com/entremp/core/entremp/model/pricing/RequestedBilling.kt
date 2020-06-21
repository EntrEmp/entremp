package com.entremp.core.entremp.model.pricing

enum class RequestedBilling {
    TYPE_A,
    TYPE_A_RETENTION,
    TYPE_M,
    TYPE_B,
    TYPE_C,
    TYPE_E;

    companion object {
        fun of(billingString: String): RequestedBilling {
            return when(billingString){
                "TYPE_A" ->
                    TYPE_A
                "TYPE_A_RETENTION" ->
                    TYPE_A_RETENTION
                "TYPE_M" ->
                    TYPE_M
                "TYPE_B" ->
                    TYPE_B
                "TYPE_C" ->
                    TYPE_C
                "TYPE_E" ->
                    TYPE_E
                else ->
                    throw RuntimeException("Invalid Requested Billing: $billingString")
            }
        }
    }

    fun toHTMLString(): String {
        return when(this){
            TYPE_A ->
                "Factura Tipo A"
            TYPE_A_RETENTION ->
                "Factura Tipo A (operacion sujeta a retencion)"
            TYPE_M ->
                "Factura Tipo M"
            TYPE_B ->
                "Factura Tipo B"
            TYPE_C ->
                "Factura Tipo C"
            TYPE_E ->
                "Factura Tipo E"
            else ->
                "RequestedBilling unsettled"
        }
    }

}