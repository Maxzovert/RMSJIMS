package com.example.rmsjims.util

import com.example.rmsjims.BuildConfig

object config {
    val SUPABASE_URL: String
        get() {
            val url = BuildConfig.SUPABASE_URL
            when {
                url.isBlank() -> error("Supabase URL missing. Define SUPABASE_URL in local.properties or environment.")
                url.contains("YOUR_SUPABASE", ignoreCase = true) -> error(
                    "Supabase URL contains placeholder value. Please replace 'YOUR_SUPABASE_PROJECT_URL_HERE' in local.properties with your actual Supabase project URL (e.g., https://xxxxx.supabase.co). Then rebuild the project."
                )
                !url.startsWith("https://") -> error("Supabase URL must start with 'https://'. Current value: $url")
                else -> return url
            }
        }

    val SUPABASE_KEY: String
        get() {
            val key = BuildConfig.SUPABASE_KEY
            when {
                key.isBlank() -> error("Supabase anon key missing. Define SUPABASE_KEY in local.properties or environment.")
                key.contains("YOUR_SUPABASE", ignoreCase = true) -> error(
                    "Supabase key contains placeholder value. Please replace 'YOUR_SUPABASE_ANON_KEY_HERE' in local.properties with your actual Supabase anon key from Settings → API. Then rebuild the project."
                )
                !key.startsWith("eyJ") -> error("Supabase key appears invalid (should start with 'eyJ'). Please verify your anon key from Supabase dashboard.")
                else -> return key
            }
        }
    
    // Fallback methods that return empty strings instead of throwing errors
    val SUPABASE_URL_OR_EMPTY: String
        get() {
            val url = BuildConfig.SUPABASE_URL
            return when {
                url.isBlank() -> ""
                url.contains("YOUR_SUPABASE", ignoreCase = true) -> ""
                !url.startsWith("https://") -> ""
                else -> url
            }
        }

    val SUPABASE_KEY_OR_EMPTY: String
        get() {
            val key = BuildConfig.SUPABASE_KEY
            return when {
                key.isBlank() -> ""
                key.contains("YOUR_SUPABASE", ignoreCase = true) -> ""
                !key.startsWith("eyJ") -> ""
                else -> key
            }
        }
    
    val isSupabaseConfigured: Boolean
        get() = SUPABASE_URL_OR_EMPTY.isNotBlank() && SUPABASE_KEY_OR_EMPTY.isNotBlank()
}