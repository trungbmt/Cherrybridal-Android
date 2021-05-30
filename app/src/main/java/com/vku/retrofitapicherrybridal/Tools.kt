package com.vku.retrofitapicherrybridal

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Tools {
    companion object {
        fun format_currency(number : Int) : String {
            val formater = NumberFormat.getCurrencyInstance()
            formater.maximumFractionDigits = 0
            formater.currency = Currency.getInstance("VND")
            return formater.format(number)
        }
        fun getDateTime(s: String): String? {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val time = sdf.parse(s)
                return SimpleDateFormat("dd-MM-yyyy HH:mm").format(time)
            } catch (e: Exception) {
                return e.toString()
            }
        }
    }
}