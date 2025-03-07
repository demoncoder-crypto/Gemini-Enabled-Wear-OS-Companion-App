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

### Dependencies

- Jetpack Compose for Wear OS
- Room for local data persistence
- Coroutines for asynchronous operations
- Horologist toolkit for Wear OS-specific UI components

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- Wear OS emulator or physical device running Wear OS 3.0+
- JDK 11

### Building and Running

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on a Wear OS emulator or device

## Future Enhancements

- Integration with actual Gemini API
- Voice-to-text and text-to-speech capabilities
- Tile support for quick access to AI assistant
- Complications for watch faces
- Cross-device synchronization with phone companion app

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Google's Wear OS design guidelines
- Android Jetpack libraries
- Horologist toolkit for Wear OS development 