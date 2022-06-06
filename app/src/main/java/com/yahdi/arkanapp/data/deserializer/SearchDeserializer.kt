package com.yahdi.arkanapp.data.deserializer

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.yahdi.arkanapp.data.response.SearchResponse
import java.lang.reflect.Type

class SearchDeserializer: JsonDeserializer<SearchResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): SearchResponse {
        return Gson().fromJson(json.asJsonObject.get("data").asJsonObject, SearchResponse::class.java)
    }

}