package com.example.gemini_enabledwearoscompanionapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gemini_enabledwearoscompanionapp.model.Conversation

/**
 * Room database for storing conversations and app data.
 */
@Database(entities = [Conversation::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class, MessageListConverter::class)
abstract class GeminiDatabase : RoomDatabase() {

    abstract fun conversationDao(): ConversationDao

    companion object {
        @Volatile
        private var INSTANCE: GeminiDatabase? = null

        fun getDatabase(context: Context): GeminiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GeminiDatabase::class.java,
                    "gemini_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 