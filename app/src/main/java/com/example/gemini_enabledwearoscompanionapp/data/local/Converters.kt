package com.example.gemini_enabledwearoscompanionapp.data.local

import androidx.room.TypeConverter
import com.example.gemini_enabledwearoscompanionapp.model.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

/**
 * Type converter for the Room database to store and retrieve complex objects.
 */
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

/**
 * Type converter for lists of Message objects.
 */
class MessageListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromMessages(messages: List<Message>): String {
        return gson.toJson(messages)
    }

    @TypeConverter
    fun toMessages(json: String): List<Message> {
        val type = object : TypeToken<List<Message>>() {}.type
        return gson.fromJson(json, type)
    }
} 