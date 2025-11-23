package com.example.rmsjims.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rmsjims.data.remote.apiservice.BranchesApiService
import com.example.rmsjims.data.remote.apiservice.ItemCategoriesApiService
import com.example.rmsjims.data.remote.apiservice.DepartmentApiService
import com.example.rmsjims.data.remote.apiservice.ItemsApiService
import com.example.rmsjims.data.remote.SessionManager
import com.example.rmsjims.data.remote.api.BranchesApi
import com.example.rmsjims.data.remote.api.ItemCategoriesApi
import com.example.rmsjims.data.remote.api.DepartmentApi
import com.example.rmsjims.data.remote.api.FacilitiesApi
import com.example.rmsjims.data.remote.api.ItemImagesApi
import com.example.rmsjims.data.remote.api.ItemSubCategoriesApi
import com.example.rmsjims.data.remote.api.ItemsApi
import com.example.rmsjims.data.remote.apiservice.FacilitesApiService
import com.example.rmsjims.data.remote.apiservice.ItemImagesApiService
import com.example.rmsjims.data.remote.apiservice.ItemSubCategoriesApiService
import com.example.rmsjims.repository.BranchRepository
import com.example.rmsjims.repository.ItemCategoriesRepository
import com.example.rmsjims.repository.DepartmentRepository
import com.example.rmsjims.repository.FacilitiesRepository
import com.example.rmsjims.repository.ItemImagesRepository
import com.example.rmsjims.repository.ItemSubCategoriesRepository
import com.example.rmsjims.repository.ItemsRepository
import com.example.rmsjims.viewmodel.BookingScreenViewmodel
import com.example.rmsjims.viewmodel.BranchViewModel
import com.example.rmsjims.viewmodel.CalendarViewModel
import com.example.rmsjims.viewmodel.ItemCategoriesViewModel
import com.example.rmsjims.viewmodel.DepartmentViewModel
import com.example.rmsjims.viewmodel.FacilitiesViewModel
import com.example.rmsjims.viewmodel.FilterSortViewModel
import com.example.rmsjims.viewmodel.ItemImagesViewModel
import com.example.rmsjims.viewmodel.ItemSubCategoriesViewModel
import com.example.rmsjims.viewmodel.ItemsViewModel
import com.example.rmsjims.viewmodel.SearchViewModel
import com.example.rmsjims.viewmodel.UserSessionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val appModule = module {

    single { SessionManager(get()) }

    // API implementations are provided conditionally:
    // - If Supabase is configured: real implementations from SupabaseModule
    // - If Supabase is not configured: fallback implementations from FallbackApiModule
    // Note: API implementations are NOT defined here to avoid dependency conflicts

    single { BranchRepository (get()) }
    single { ItemCategoriesRepository(get()) }
    single { ItemsRepository(get()) }
    single { DepartmentRepository(get()) }
    single { FacilitiesRepository(get()) }
    single { ItemImagesRepository(get()) }
    single { ItemSubCategoriesRepository(get()) }

//    // ViewModels
    viewModel { ItemCategoriesViewModel(get()) }
    viewModel { ItemsViewModel(get()) }
    viewModel { ItemSubCategoriesViewModel(get()) }
    viewModel { ItemImagesViewModel(get()) }
    viewModel { UserSessionViewModel(get()) }
    viewModel { BookingScreenViewmodel() }
    viewModel { CalendarViewModel() }
    viewModel { FilterSortViewModel() }
    viewModel { SearchViewModel() }
    viewModel { BranchViewModel(get()) }
    viewModel { FacilitiesViewModel(get()) }
    viewModel { DepartmentViewModel(get()) }
}



// `single<InventoryApiService>` means that Koin will create only one instance of `InventoryApiService`
// throughout the application's lifecycle.
// It creates an instance of `SupabaseInventoryApi`.
//    // `get()` within the lambda is a Koin function that resolves and provides any dependencies
//    single<InventoryApiService> { SupabaseInventoryApi(get()) }
//
//    single { InventoryRepository(get()) }
//
//    single { InventoryImageViewModel(get()) }