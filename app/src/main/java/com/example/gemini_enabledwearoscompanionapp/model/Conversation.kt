package com.example.gemini_enabledwearoscompanionapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.gemini_enabledwearoscompanionapp.data.local.MessageListConverter
import java.util.Date

/**
 * Represents a conversation between the user and Gemini AI.
 */
@Entity(tableName = "conversations")
@TypeConverters(MessageListConverter::class)
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val messages: List<Message> = emptyList(),
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

/**
 * Represents a single message in a conversation.
 */
data class Message(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val timestamp: Date = Date(),
    val isFromUser: Boolean,
    val isProcessing: Boolean = false
) 