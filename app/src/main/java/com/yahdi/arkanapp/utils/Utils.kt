package com.yahdi.arkanapp.utils

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import java.text.DateFormat

object Utils {
    fun getDefaultGson(): GsonBuilder = GsonBuilder()
        .enableComplexMapKeySerialization()
        .serializeNulls()
        .setDateFormat(DateFormat.LONG)
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .setPrettyPrinting()
        .setVersion(1.0)
}