package com.example.gemini_enabledwearoscompanionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.gemini_enabledwearoscompanionapp.ui.screens.ConversationScreen
import com.example.gemini_enabledwearoscompanionapp.ui.screens.HomeScreen
import com.example.gemini_enabledwearoscompanionapp.ui.theme.GeminiEnabledWearOSCompanionAppTheme
import com.example.gemini_enabledwearoscompanionapp.viewmodel.ConversationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeminiEnabledWearOSCompanionAppTheme {
                WearApp()
            }
        }
    }
}

@Composable
fun WearApp() {
    val navController = rememberSwipeDismissableNavController()
    val conversationViewModel: ConversationViewModel = viewModel()
    
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = conversationViewModel,
                onNavigateToConversation = { conversationId ->
                    navController.navigate("conversation/$conversationId")
                }
            )
        }
        
        composable(
            route = "conversation/{conversationId}",
            arguments = listOf(
                navArgument("conversationId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getLong("conversationId") ?: 0L
            
            // Load the conversation
            remember(conversationId) {
                conversationViewModel.loadConversation(conversationId)
                conversationId
            }
            
            ConversationScreen(
                viewModel = conversationViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GeminiEnabledWearOSCompanionAppTheme {
        WearApp()
    }
}