package com.example.gemini_enabledwearoscompanionapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gemini_enabledwearoscompanionapp.model.Conversation

/**
 * Data Access Object for Conversation entities.
 * Provides methods to query, insert, update, and delete conversations from the database.
 */
@Dao
interface ConversationDao {
    
    @Query("SELECT * FROM conversations ORDER BY updatedAt DESC")
    fun getAllConversations(): LiveData<List<Conversation>>
    
    @Query("SELECT * FROM conversations WHERE id = :id")
    fun getConversationById(id: Long): LiveData<Conversation>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation): Long
    
    @Update
    suspend fun updateConversation(conversation: Conversation)
    
    @Delete
    suspend fun deleteConversation(conversation: Conversation)
    
    @Query("DELETE FROM conversations")
    suspend fun deleteAllConversations()
} 