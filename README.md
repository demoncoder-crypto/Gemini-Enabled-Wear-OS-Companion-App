# Gemini-Enabled Wear OS Companion App

A smart, AI-powered companion app for Wear OS devices that integrates with Gemini to provide intelligent, context-aware assistance directly on your wrist.

## Features

- **Conversational AI Interface**: Natural language interaction via text and voice
- **Intelligent Responses**: Simulated Gemini AI backend that provides contextual responses
- **Optimized for Wear OS**: UI specifically designed for small screens and wearable interactions
- **Offline Capabilities**: Local storage of conversations for offline access
- **Battery-Efficient**: Designed with battery optimization in mind

## Technical Overview

### Architecture

This app follows the MVVM (Model-View-ViewModel) architecture pattern and leverages Android Jetpack components:

- **UI Layer**: Jetpack Compose with Wear OS specific components
- **Business Logic**: ViewModels with LiveData
- **Data Layer**: Room database for local storage
- **Background Processing**: WorkManager and Services

### Key Components

- **ConversationViewModel**: Manages conversation state and interactions with the AI service
- **GeminiAIService**: Handles AI processing in the background
- **Room Database**: Stores conversation history for offline access
- **Wear OS Navigation**: SwipeDismissableNavHost for intuitive navigation

