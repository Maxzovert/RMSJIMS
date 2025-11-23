package com.example.rmsjims

//config: A Kotlin object (likely in util/config.kt) that holds your Supabase URL & Key securely (probably pulled from BuildConfig or a secure source).
//appModule: Your main Koin module for app-level dependencies (like ViewModels, Repositories, etc.).
//supabaseModule: The DI module for setting up your Supabase client.
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.rmsjims.util.config
import com.example.rmsjims.di.appModule
import com.example.rmsjims.di.supabaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

//Application - base class for maintaining global application state in Android. - acts as the enry point for the app
class RMSJimsApp : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        
        Log.d("RMSJimsApp", "Application onCreate() called")
        
        try {
            // Use fallback methods to allow app to start without Supabase
            val supabaseUrl = config.SUPABASE_URL_OR_EMPTY
            val supabaseKey = config.SUPABASE_KEY_OR_EMPTY
            val isSupabaseConfigured = config.isSupabaseConfigured
            
            Log.d("RMSJimsApp", "Supabase URL loaded: ${if (supabaseUrl.isNotBlank()) "${supabaseUrl.take(20)}..." else "EMPTY (fallback mode)"}")
            Log.d("RMSJimsApp", "Supabase Key loaded: ${if (supabaseKey.isNotBlank()) "${supabaseKey.take(20)}..." else "EMPTY (fallback mode)"}")
            Log.d("RMSJimsApp", "Supabase configured: $isSupabaseConfigured")
            
            startKoin {
                androidContext(this@RMSJimsApp)        // Supplies the app-level context for DI —
                modules(    //Registers your DI modules
                    if (isSupabaseConfigured) {
                        supabaseModule(supabaseUrl, supabaseKey)
                    } else {
                        Log.w("RMSJimsApp", "Supabase not configured - skipping Supabase module initialization")
                        module { } // Empty module as fallback
                    },
                    appModule
                )
            }
            Log.d("RMSJimsApp", "Koin initialized successfully")
        } catch (e: Exception) {
            Log.e("RMSJimsApp", "CRITICAL ERROR initializing app", e)
            Log.e("RMSJimsApp", "Exception type: ${e.javaClass.simpleName}")
            Log.e("RMSJimsApp", "Exception message: ${e.message}")
            throw e
        }
    }
}
