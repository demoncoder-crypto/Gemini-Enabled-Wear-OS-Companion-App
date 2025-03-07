package com.example.gemini_enabledwearoscompanionapp.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gemini_enabledwearoscompanionapp.data.local.GeminiDatabase
import com.example.gemini_enabledwearoscompanionapp.model.Conversation
import com.example.gemini_enabledwearoscompanionapp.model.Message
import com.example.gemini_enabledwearoscompanionapp.repository.ConversationRepository
import com.example.gemini_enabledwearoscompanionapp.service.GeminiAIService
import kotlinx.coroutines.launch
import java.util.Date

/**
 * ViewModel for managing conversation data and UI state.
 */
class ConversationViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "ConversationViewModel"
    
    // Repository for data operations
    private val repository: ConversationRepository
    
    // All conversations
    val allConversations: LiveData<List<Conversation>>
    
    // Current conversation
    private val _currentConversation = MutableLiveData<Conversation>()
    val currentConversation: LiveData<Conversation> = _currentConversation
    
    // Loading state
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    // Error state
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error
    
    // Service connection
    private var geminiService: GeminiAIService? = null
    private var isBound = false
    
    // Service connection object
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as GeminiAIService.LocalBinder
            geminiService = binder.getService()
            isBound = true
            Log.d(TAG, "Service connected")
        }
        
        override fun onServiceDisconnected(name: ComponentName?) {
            geminiService = null
            isBound = false
            Log.d(TAG, "Service disconnected")
        }
    }
    
    init {
        // Initialize repository
        val conversationDao = GeminiDatabase.getDatabase(application).conversationDao()
        repository = ConversationRepository(conversationDao)
        
        // Initialize all conversations
        allConversations = repository.allConversations
        
        // Bind to the service
        bindService()
    }
    
    /**
     * Bind to the GeminiAIService
     */
    private fun bindService() {
        Intent(getApplication(), GeminiAIService::class.java).also { intent ->
            getApplication<Application>().bindService(
                intent,
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }
    
    /**
     * Load a conversation by ID
     */
    fun loadConversation(conversationId: Long) {
        viewModelScope.launch {
            try {
                val conversation = repository.getConversationById(conversationId)
                _currentConversation.value = conversation.value
            } catch (e: Exception) {
                _error.value = "Failed to load conversation: ${e.message}"
            }
        }
    }
    
    /**
     * Create a new conversation
     */
    fun createNewConversation(title: String = "New Conversation") {
        viewModelScope.launch {
            try {
                val conversationId = repository.createConversation(title)
                loadConversation(conversationId)
            } catch (e: Exception) {
                _error.value = "Failed to create conversation: ${e.message}"
            }
        }
    }
    
    /**
     * Send a message to the AI
     */
    fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        viewModelScope.launch {
            try {
                // Create user message
                val userMessage = Message(
                    content = content,
                    isFromUser = true
                )
                
                // Add user message to conversation
                _currentConversation.value?.let { conversation ->
                    val updatedConversation = repository.addMessage(conversation, userMessage)
                    _currentConversation.value = updatedConversation
                    
                    // Show loading state
                    _isLoading.value = true
                    
                    // Process with AI service
                    geminiService?.processUserMessage(content, object : GeminiAIService.AIResponseCallback {
                        override fun onResponseReceived(response: Message) {
                            viewModelScope.launch {
                                // Add AI response to conversation
                                val conversationWithResponse = repository.addMessage(updatedConversation, response)
                                _currentConversation.value = conversationWithResponse
                                _isLoading.value = false
                            }
                        }
                        
                        override fun onError(error: String) {
                            _error.value = error
                            _isLoading.value = false
                        }
                    })
                }
            } catch (e: Exception) {
                _error.value = "Failed to send message: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        _error.value = null
    }
    
    override fun onCleared() {
        super.onCleared()
        // Unbind from service when ViewModel is cleared
        if (isBound) {
            getApplication<Application>().unbindService(serviceConnection)
            isBound = false
        }
    }
} 