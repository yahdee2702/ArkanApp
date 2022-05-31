package com.yahdi.arkanapp.data.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.yahdi.arkanapp.data.response.SurahResponse
import java.lang.reflect.Type

class SurahDeserializer:JsonDeserializer<SurahResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): SurahResponse {
        val data = json.asJsonObject.get("data")

        return TODO("LATER")
    }

}