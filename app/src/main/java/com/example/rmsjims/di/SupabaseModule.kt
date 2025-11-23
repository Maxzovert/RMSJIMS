package com.example.rmsjims.di

//SupabaseClient: the core Supabase client you'll inject into your services.
//createSupabaseClient: helper to build and configure the Supabase client.
//Postgrest: plugin that lets you interact with Supabase's database (tables).
//module: part of Koin DSL to define dependencies.

import android.util.Log
import com.example.rmsjims.data.remote.apiservice.BranchesApiService
import com.example.rmsjims.data.remote.apiservice.ItemCategoriesApiService
import com.example.rmsjims.data.remote.apiservice.DepartmentApiService
import com.example.rmsjims.data.remote.apiservice.ItemsApiService
import com.example.rmsjims.data.remote.apiservice.FacilitesApiService
import com.example.rmsjims.data.remote.apiservice.ItemImagesApiService
import com.example.rmsjims.data.remote.apiservice.ItemSubCategoriesApiService
import com.example.rmsjims.data.remote.api.BranchesApi
import com.example.rmsjims.data.remote.api.ItemCategoriesApi
import com.example.rmsjims.data.remote.api.DepartmentApi
import com.example.rmsjims.data.remote.api.FacilitiesApi
import com.example.rmsjims.data.remote.api.ItemImagesApi
import com.example.rmsjims.data.remote.api.ItemSubCategoriesApi
import com.example.rmsjims.data.remote.api.ItemsApi
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

import org.koin.dsl.module

//efining a Koin module named supabaseModule that takes your Supabase project's URL and Key as parameters.
fun supabaseModule(
    supabaseUrl: String,
    supabaseKey: String
) = module {
    single<SupabaseClient> {        //singleton
        Log.d("SupabaseModule", "Creating Supabase client...")
        Log.d("SupabaseModule", "URL: $supabaseUrl")
        Log.d("SupabaseModule", "Key prefix: ${supabaseKey.take(20)}...")
        try {
            val client = createSupabaseClient(
                supabaseUrl = supabaseUrl,
                supabaseKey = supabaseKey
            ){
                install(Postgrest)  //install the PostgREST plugin to query tables like a REST API
            }
            Log.d("SupabaseModule", "Supabase client created successfully")
            client
        } catch (e: Exception) {
            Log.e("SupabaseModule", "FAILED to create Supabase client", e)
            Log.e("SupabaseModule", "Exception type: ${e.javaClass.simpleName}")
            Log.e("SupabaseModule", "Exception message: ${e.message}")
            throw e
        }
    }
    
    // Provide real API implementations when Supabase is configured
    single<ItemCategoriesApiService> { ItemCategoriesApi(get()) }
    single<ItemsApiService> { ItemsApi(get()) }
    single<BranchesApiService> { BranchesApi(get()) }
    single<DepartmentApiService> { DepartmentApi(get()) }
    single<FacilitesApiService> { FacilitiesApi(get()) }
    single<ItemImagesApiService> { ItemImagesApi(get()) }
    single<ItemSubCategoriesApiService> { ItemSubCategoriesApi(get()) }
}
