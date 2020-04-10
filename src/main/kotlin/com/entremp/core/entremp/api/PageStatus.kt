package com.entremp.core.entremp.api

data class PageStatus(
    val first: String,
    val last: String
) {
    companion object{
        fun inactiveFirst(): PageStatus = PageStatus(
            first = "inactive",
            last = "waves-effect"
        )

        fun inactiveLast(): PageStatus = PageStatus(
            first = "waves-effect",
            last = "inactive"
        )

        fun bothActive(): PageStatus = PageStatus(
            first = "waves-effect",
            last = "waves-effect"
        )

        fun bothInactive(): PageStatus = PageStatus(
            first = "inactive",
            last = "inactive"
        )
    }
}