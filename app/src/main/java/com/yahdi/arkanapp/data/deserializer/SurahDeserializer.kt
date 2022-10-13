package com.yahdi.arkanapp.data.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.data.response.SurahResponse
import com.yahdi.arkanapp.utils.Utils
import java.lang.reflect.Type

class SurahDeserializer : JsonDeserializer<SurahResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): SurahResponse {
        val mGson = Utils.getDefaultGson()
            .registerTypeAdapter(AyahResponse::class.java, AyahDeserializer())
            .create()

        val data = json.asJsonObject.get("data").asJsonArray
        val arabicData = data.get(0).asJsonObject
        val translatedData = data.get(1).asJsonObject
        val transliterationData = data.get(2).asJsonObject

        val surahData = mGson.fromJson(arabicData, SurahResponse::class.java)

        val arabicAyahs = arabicData.get("ayahs").asJsonArray
        val translatedAyahs = translatedData.get("ayahs").asJsonArray
        val transliterationAyahs = transliterationData.get("ayahs").asJsonArray

        val ayahList = arrayListOf<AyahResponse>()
        arabicAyahs.forEach {
            val index = arabicAyahs.indexOf(it)
            val tempList = listOf(
                it.asJsonObject,
                translatedAyahs.get(index).asJsonObject,
                transliterationAyahs.get(index).asJsonObject,
            )
            val jsonTempList = mGson.toJsonTree(tempList)
            val ayah = mGson.fromJson(jsonTempList, AyahResponse::class.java)
            ayahList.add(ayah)
        }
        surahData.ayahs = ayahList

        return surahData
    }

}