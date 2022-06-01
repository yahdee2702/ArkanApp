package com.yahdi.arkanapp.data.deserializer

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.yahdi.arkanapp.data.response.SurahResponse
import java.lang.reflect.Type

class SurahListDeserializer: JsonDeserializer<List<SurahResponse>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<SurahResponse> {
        val mGson = Gson()
        val data = json.asJsonObject.get("data").asJsonArray
        val tempList = arrayListOf<SurahResponse>()
        data.forEach {
            val parsedData = mGson.fromJson(it.asJsonObject, SurahResponse::class.java)
            tempList.add(parsedData)
        }

        return tempList.toList()
    }

}