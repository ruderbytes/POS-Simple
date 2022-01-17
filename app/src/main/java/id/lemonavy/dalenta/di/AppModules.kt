package id.lemonavy.dalenta.di

import android.app.Application
import android.content.SharedPreferences
import id.lemonavy.dalenta.ui.product.ProductViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val baseModule = module {

    single{ getSharedPref(androidApplication()) }
    single<SharedPreferences.Editor> { getSharedPref(androidApplication()).edit() }

    viewModel {
        ProductViewModel(
            get()
        )
    }
}


fun getSharedPref(androidApplication: Application): SharedPreferences {
    return  androidApplication.getSharedPreferences("default_sales",
        android.content.Context.MODE_PRIVATE)
}