package com.vku.retrofitapicherrybridal

import java.text.NumberFormat
import java.util.*

class Tools {
    companion object {
        fun format_currency(number : Int) : String {
            val formater = NumberFormat.getCurrencyInstance()
            formater.maximumFractionDigits = 0
            formater.currency = Currency.getInstance("VND")
            return formater.format(number)
        }
    }
}