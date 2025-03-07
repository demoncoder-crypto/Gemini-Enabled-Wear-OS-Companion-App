package com.example.gemini_enabledwearoscompanionapp

import android.app.Application
import androidx.work.Configuration

/**
 * Application class for the Gemini Wear OS Companion App.
 * Handles initialization of app components and services.
 */
class GeminiWearApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        // Initialize components here when app starts
    }

    /**
     * Provides configuration for WorkManager
     * This helps with efficient background processing
     */
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }
} 