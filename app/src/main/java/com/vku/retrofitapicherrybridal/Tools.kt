package com.vku.retrofitapicherrybridal

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
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
        fun getRealPathFromURI(contentUri: Uri, context : Context): String? {
            var path: String? = null
            val proj = arrayOf(MediaStore.MediaColumns.DATA)
            val cursor: Cursor? =
                    context.getContentResolver()?.query(contentUri, proj, null, null, null)
            if (cursor?.moveToFirst()!!) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                path = cursor.getString(column_index)
            }
            cursor.close()
            return path
        }
    }
}