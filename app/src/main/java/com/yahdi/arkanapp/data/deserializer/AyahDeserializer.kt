package com.yahdi.arkanapp.data.deserializer

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.yahdi.arkanapp.data.response.AyahResponse
import java.lang.reflect.Type

class AyahDeserializer: JsonDeserializer<AyahResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): AyahResponse {
        val data = if(json.isJsonObject)
            json.asJsonObject.get("data").asJsonArray
        else
            json.asJsonArray

        val ayah = Gson().fromJson(data.get(0).toString(), AyahResponse::class.java)
        ayah.translation = data.get(1).asJsonObject.get("text").asString
        
        return ayah;
    }

}