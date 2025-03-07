package com.example.gemini_enabledwearoscompanionapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import com.example.gemini_enabledwearoscompanionapp.model.Conversation
import com.example.gemini_enabledwearoscompanionapp.model.Message
import com.example.gemini_enabledwearoscompanionapp.ui.components.MessageItem
import com.example.gemini_enabledwearoscompanionapp.viewmodel.ConversationViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.compose.ui.text.input.TextFieldValue

/**
 * Screen for displaying and interacting with a conversation.
 */
@Composable
fun ConversationScreen(
    viewModel: ConversationViewModel,
    onNavigateBack: () -> Unit
) {
    val conversation by viewModel.currentConversation.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    
    ConversationContent(
        conversation = conversation,
        isLoading = isLoading,
        error = error,
        onSendMessage = { viewModel.sendMessage(it) },
        onClearError = { viewModel.clearError() }
    )
}

@Composable
fun ConversationContent(
    conversation: Conversation?,
    isLoading: Boolean,
    error: String?,
    onSendMessage: (String) -> Unit,
    onClearError: () -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val listState = rememberScalingLazyListState()
    
    // Scroll to bottom when new messages arrive
    LaunchedEffect(conversation?.messages?.size) {
        conversation?.messages?.size?.let { size ->
            if (size > 0) {
                listState.scrollToItem(size - 1)
            }
        }
    }
    
    // Show error if present
    error?.let {
        LaunchedEffect(error) {
            // In a real app, you might want to show a snackbar or dialog
            // For now, we'll just clear the error after a delay
            kotlinx.coroutines.delay(3000)
            onClearError()
        }
    }
    
    Scaffold(
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Error message
                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                
                // Input field
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    TextField(
                        value = textFieldValue,
                        onValueChange = { textFieldValue = it },
                        placeholder = { Text("Message Gemini...") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (textFieldValue.text.isNotBlank()) {
                                        onSendMessage(textFieldValue.text)
                                        textFieldValue = TextFieldValue("")
                                    }
                                },
                                enabled = !isLoading && textFieldValue.text.isNotBlank()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send"
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        if (conversation == null) {
            // Show empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Start a conversation with Gemini",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            // Show conversation
            ScalingLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = 8.dp),
                state = listState
            ) {
                items(conversation.messages) { message ->
                    MessageItem(message = message)
                }
                
                // Show loading indicator if waiting for response
                if (isLoading) {
                    item {
                        MessageItem(
                            message = Message(
                                content = "",
                                isFromUser = false,
                                isProcessing = true
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VoiceInputChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Chip(
        onClick = onClick,
        colors = ChipDefaults.primaryChipColors(),
        label = { Text("Voice") },
        modifier = modifier
    )
} 