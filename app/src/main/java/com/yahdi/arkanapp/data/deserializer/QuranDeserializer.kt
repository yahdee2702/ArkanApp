package com.yahdi.arkanapp.data.deserializer

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.yahdi.arkanapp.data.response.QuranResponse
import java.lang.reflect.Type

class QuranDeserializer: JsonDeserializer<QuranResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): QuranResponse {
        return Gson().fromJson(json, QuranResponse::class.java)
    }
}