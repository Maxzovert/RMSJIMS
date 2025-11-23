package com.example.rmsjims.di

import android.util.Log
import com.example.rmsjims.data.remote.apiservice.BranchesApiService
import com.example.rmsjims.data.remote.apiservice.ItemCategoriesApiService
import com.example.rmsjims.data.remote.apiservice.DepartmentApiService
import com.example.rmsjims.data.remote.apiservice.ItemsApiService
import com.example.rmsjims.data.remote.apiservice.FacilitesApiService
import com.example.rmsjims.data.remote.apiservice.ItemImagesApiService
import com.example.rmsjims.data.remote.apiservice.ItemSubCategoriesApiService
import com.example.rmsjims.data.schema.*
import org.koin.dsl.module

/**
 * Fallback API implementations that return empty lists when Supabase is not configured.
 * This allows the app to run without Supabase credentials.
 */
val fallbackApiModule = module {
    single<ItemCategoriesApiService> {
        object : ItemCategoriesApiService {
            override suspend fun getCategories(): List<ItemCategories> {
                Log.w("FallbackApi", "ItemCategoriesApi: Supabase not configured, returning empty list")
                return emptyList()
            }
        }
    }
    
    single<ItemsApiService> {
        object : ItemsApiService {
            override suspend fun getItems(): List<Items> {
                Log.w("FallbackApi", "ItemsApi: Supabase not configured, returning empty list")
                return emptyList()
            }
        }
    }
    
    single<BranchesApiService> {
        object : BranchesApiService {
            override suspend fun getBranches(): List<Branch> {
                Log.w("FallbackApi", "BranchesApi: Supabase not configured, returning empty list")
                return emptyList()
            }
        }
    }
    
    single<DepartmentApiService> {
        object : DepartmentApiService {
            override suspend fun getDepartments(): List<Department> {
                Log.w("FallbackApi", "DepartmentApi: Supabase not configured, returning empty list")
                return emptyList()
            }
        }
    }
    
    single<FacilitesApiService> {
        object : FacilitesApiService {
            override suspend fun getFacilities(): List<Facilities> {
                Log.w("FallbackApi", "FacilitiesApi: Supabase not configured, returning empty list")
                return emptyList()
            }
        }
    }
    
    single<ItemImagesApiService> {
        object : ItemImagesApiService {
            override suspend fun getItems(): List<ItemImages> {
                Log.w("FallbackApi", "ItemImagesApi.getItems: Supabase not configured, returning empty list")
                return emptyList()
            }
            
            override suspend fun getItemById(itemId: Int): ItemImages? {
                Log.w("FallbackApi", "ItemImagesApi.getItemById: Supabase not configured, returning null")
                return null
            }
            
            override suspend fun addItem(itemImages: ItemImages): ItemImages {
                Log.w("FallbackApi", "ItemImagesApi.addItem: Supabase not configured, returning original item")
                return itemImages
            }
            
            override suspend fun deleteItem(itemId: Int): Boolean {
                Log.w("FallbackApi", "ItemImagesApi.deleteItem: Supabase not configured, returning false")
                return false
            }
            
            override suspend fun updateItem(itemImages: ItemImages): ItemImages {
                Log.w("FallbackApi", "ItemImagesApi.updateItem: Supabase not configured, returning original item")
                return itemImages
            }
        }
    }
    
    single<ItemSubCategoriesApiService> {
        object : ItemSubCategoriesApiService {
            override suspend fun getItemSubCategories(): List<ItemSubCategories> {
                Log.w("FallbackApi", "ItemSubCategoriesApi: Supabase not configured, returning empty list")
                return emptyList()
            }
        }
    }
}

