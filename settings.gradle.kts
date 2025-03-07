pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    
    // Add version catalog with the kapt plugin
    versionCatalogs {
        create("libs") {
            // Keep existing plugins
            plugin("android.application", "com.android.application").version("8.1.0")
            plugin("kotlin.android", "org.jetbrains.kotlin.android").version("1.9.0")
            plugin("kotlin.compose", "org.jetbrains.compose").version("1.5.1")
            // Add the kapt plugin
            plugin("kotlin.kapt", "org.jetbrains.kotlin.kapt").version("1.9.0")
        }
    }
}

rootProject.name = "Gemini-Enabled Wear OS Companion App"
include(":app")
 