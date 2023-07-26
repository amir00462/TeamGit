package ir.dunijet.teamgit.di

import ir.dunijet.teamgit.data.net.createApiService
import ir.dunijet.teamgit.data.repository.BlogRepository
import ir.dunijet.teamgit.data.repository.RetrofitBlogRepository
import ir.dunijet.teamgit.ui.fetures.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {
    single { createApiService() }
    single<BlogRepository> { RetrofitBlogRepository(get()) }

    viewModel { HomeViewModel(get()) }
}

