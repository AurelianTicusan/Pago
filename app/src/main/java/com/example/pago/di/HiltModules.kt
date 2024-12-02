package com.example.pago.di

import com.example.pago.BASE_URL
import com.example.pago.data.framework.PersonsRepositoryImpl
import com.example.pago.data.network.PagoApi
import com.example.pago.domain.PersonsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun providesPetFinderApi(): PagoApi {
        val interceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        return retrofit.create(PagoApi::class.java)
    }

}

@InstallIn(SingletonComponent::class)
@Module
abstract class PersonsRepositoryModule {
    @Binds
    abstract fun bindNavigator(impl: PersonsRepositoryImpl): PersonsRepository
}