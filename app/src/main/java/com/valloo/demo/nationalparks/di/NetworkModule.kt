package com.valloo.demo.nationalparks.di

import com.valloo.demo.nationalparks.BuildConfig
import com.valloo.demo.nationalparks.infra.http.EventsApi
import com.valloo.demo.nationalparks.infra.http.ParksApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        httpClient.addInterceptor(logging)

        httpClient.addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
                .header("X-Api-Key", BuildConfig.NPS_API_KEY)

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(
                RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io())
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideParksApi(retrofit: Retrofit): ParksApi {
        return retrofit.create(ParksApi::class.java)
    }

    @Singleton
    @Provides
    fun provideEventsApi(retrofit: Retrofit): EventsApi {
        return retrofit.create(EventsApi::class.java)
    }

    companion object {
        private const val BASE_URL = "https://developer.nps.gov/api/v1/"
    }
}