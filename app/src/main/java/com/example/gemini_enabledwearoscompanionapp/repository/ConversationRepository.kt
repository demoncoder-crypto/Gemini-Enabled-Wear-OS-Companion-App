package com.example.gemini_enabledwearoscompanionapp.repository

import androidx.lifecycle.LiveData
import com.example.gemini_enabledwearoscompanionapp.data.local.ConversationDao
import com.example.gemini_enabledwearoscompanionapp.model.Conversation
import com.example.gemini_enabledwearoscompanionapp.model.Message
import java.util.Date

/**
 * Repository that handles conversation data operations.
 * Acts as a single source of truth for accessing conversation data from both local and remote sources.
 */
class ConversationRepository(private val conversationDao: ConversationDao) {

    // Get all conversations from the database
    val allConversations: LiveData<List<Conversation>> = conversationDao.getAllConversations()

    // Get a specific conversation by ID
    fun getConversationById(id: Long): LiveData<Conversation> {
        return conversationDao.getConversationById(id)
    }

    // Create a new conversation
    suspend fun createConversation(title: String): Long {
        val conversation = Conversation(
            title = title,
            messages = emptyList()
        )
        return conversationDao.insertConversation(conversation)
    }

    // Add a message to a conversation
    suspend fun addMessage(conversation: Conversation, message: Message): Conversation {
        val updatedMessages = conversation.messages + message
        val updatedConversation = conversation.copy(
            messages = updatedMessages,
            updatedAt = Date()
        )
        conversationDao.updateConversation(updatedConversation)
        return updatedConversation
    }

    // Delete a conversation
    suspend fun deleteConversation(conversation: Conversation) {
        conversationDao.deleteConversation(conversation)
    }

    // Clear all conversations
    suspend fun clearAllConversations() {
        conversationDao.deleteAllConversations()
    }
} 