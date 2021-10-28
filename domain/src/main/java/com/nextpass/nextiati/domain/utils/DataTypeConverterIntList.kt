package com.nextpass.nextiati.domain.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class DataTypeConverterIntList {
    private val gson: Gson = Gson()

    @TypeConverter
    fun stringToList(data: String?): List<Int?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Int?>?>() {}.getType()
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun ListToString(someObjects: List<Int?>?): String? {
        return gson.toJson(someObjects)
    }
}