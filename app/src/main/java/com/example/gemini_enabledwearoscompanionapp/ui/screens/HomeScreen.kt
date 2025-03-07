package com.example.gemini_enabledwearoscompanionapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import com.example.gemini_enabledwearoscompanionapp.model.Conversation
import com.example.gemini_enabledwearoscompanionapp.viewmodel.ConversationViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Home screen for the app that displays conversation history and allows starting new conversations.
 */
@Composable
fun HomeScreen(
    viewModel: ConversationViewModel,
    onNavigateToConversation: (Long) -> Unit
) {
    val conversations by viewModel.allConversations.observeAsState(emptyList())
    
    HomeContent(
        conversations = conversations,
        onConversationClick = onNavigateToConversation,
        onNewConversation = {
            viewModel.createNewConversation()
            // The ViewModel will create a new conversation and update the currentConversation LiveData
            viewModel.currentConversation.value?.let { conversation ->
                onNavigateToConversation(conversation.id)
            }
        }
    )
}

@Composable
fun HomeContent(
    conversations: List<Conversation>,
    onConversationClick: (Long) -> Unit,
    onNewConversation: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (conversations.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No conversations yet",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                
                Button(
                    onClick = onNewConversation,
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.primaryButtonColors()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Conversation",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Start",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        } else {
            // List of conversations
            val listState = rememberScalingLazyListState()
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 8.dp,
                    end = 8.dp,
                    top = 32.dp,
                    bottom = 32.dp
                ),
                state = listState
            ) {
                // New conversation button at the top
                item {
                    Chip(
                        onClick = onNewConversation,
                        colors = ChipDefaults.primaryChipColors(),
                        label = { Text("New Conversation") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "New Conversation",
                                modifier = Modifier.size(ChipDefaults.IconSize)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // List of existing conversations
                items(conversations) { conversation ->
                    ConversationItem(
                        conversation = conversation,
                        onClick = { onConversationClick(conversation.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
    val lastMessage = conversation.messages.lastOrNull()?.content ?: "No messages"
    
    Chip(
        onClick = onClick,
        colors = ChipDefaults.secondaryChipColors(),
        label = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = conversation.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = lastMessage,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = dateFormat.format(conversation.updatedAt),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
} 